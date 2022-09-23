package hedera.starter.cowUser;

import com.hedera.hashgraph.sdk.*;
import hedera.starter.cowUser.models.User;
import hedera.starter.cowUser.models.UserAuthResponseDTO;
import hedera.starter.cowUser.models.UserFullInfoDTO;
import hedera.starter.cowUserType.models.UserType;
import hedera.starter.utilities.EnvUtils;
import hedera.starter.utilities.JwtToken;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
public class UserService {
    public Client client = Client.forTestnet();
    private String PATH_USER_BIN = "D:\\Bernardo\\PolitecnicoLeiria\\MEI_CM\\2ano\\final_project\\COW.API\\src\\main\\java\\hedera\\starter\\cowUser\\User.bin";

    public UserFullInfoDTO convertToDTO(User user, String userType){
        return new UserFullInfoDTO(user.getIdUser(), user.getIdContract(), user.getIdWallet(), user.getIdUserType(), userType,
                user.getName(), user.getBirthDate(), user.getEmail(), user.getPassword(), user.getActive(),
                user.getBalance());
    }

    public UserFullInfoDTO getUserDTO(User user, UserType userType){
        return convertToDTO(user, userType.getDescription().toUpperCase());
    }

    public UserAuthResponseDTO authenticate(User user, UserType userType, JwtToken jwtToken) {
        UserAuthResponseDTO userAuthResponseDTO = new UserAuthResponseDTO();
        try {
            UserFullInfoDTO userFullInfoDTO = convertToDTO(user, userType.getDescription().toUpperCase());
            if (userFullInfoDTO != null) {
                String token = jwtToken.generateToken(user);
                userAuthResponseDTO.setToken(token);
                userAuthResponseDTO.setUser(userFullInfoDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userAuthResponseDTO;
    }

    public List<String> createUser(int idUserType, String name, Date birthDate, String email, String password,
                                   Boolean active) {
        List<String> newUserObject = new ArrayList<>();
        try {
            File myObj = new File(PATH_USER_BIN);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                PrivateKey operatorKey = EnvUtils.getOperatorKey();
                client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

                ContractId newContractId;
                AccountId newAccountId;
                PrivateKey newAccountPrivateKey = PrivateKey.generateED25519();
                PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();

                TransactionResponse newAccount = new AccountCreateTransaction()
                        .setKey(newAccountPublicKey)
                        .setInitialBalance( Hbar.fromTinybars(1000))
                        .execute(client);

                newAccountId = newAccount.getReceipt(client).accountId;

                AccountBalance tokenBalance = null;
                if (newAccountId != null){
                    AccountBalanceQuery query = new AccountBalanceQuery()
                            .setAccountId(newAccountId);
                    tokenBalance = query.execute(client);
                }

                if (tokenBalance != null){
                    String objectByteCode = myReader.nextLine();
                    byte[] bytecode = objectByteCode.getBytes(StandardCharsets.UTF_8);

                    TransactionResponse fileCreateTx = new FileCreateTransaction()
                            .setKeys(operatorKey)
                            .freezeWith(client)
                            .execute(client);

                    TransactionReceipt fileReceipt1 = fileCreateTx.getReceipt(client);
                    FileId bytecodeFileId = fileReceipt1.fileId;

                    if (bytecodeFileId != null) {
                        TransactionResponse fileAppendTransaction = new FileAppendTransaction()
                                .setFileId(bytecodeFileId)
                                .setContents(bytecode)
                                .setMaxChunks(10)
                                .setMaxTransactionFee(new Hbar(2))
                                .execute(client);
                        TransactionReceipt fileReceipt2 = fileAppendTransaction.getReceipt(client);

                        System.out.println("Contract Created =" + fileReceipt2);

                        TransactionResponse contractCreateTransaction = new ContractCreateTransaction()
                                .setBytecodeFileId(bytecodeFileId)
                                .setGas(3000000)
                                .setAdminKey(operatorKey)
                                .setConstructorParameters(new ContractFunctionParameters().addUint32(idUserType)
                                        .addString(name).addString(birthDate.toString()).addString(email)
                                        .addString(password).addBool(active).addString(tokenBalance.hbars.toString()))
                                .execute(client);

                        TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                        System.out.println("Contract Filled " + fileReceipt3.contractId);

                        newContractId = fileReceipt3.contractId;

                        if (newContractId != null) {
                            newUserObject.add(newContractId.toString());
                            newUserObject.add(newAccountId.toString());
                            newUserObject.add(tokenBalance.hbars.toString());
                        }
                    }
                }
            }

        } catch (FileNotFoundException | TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }

        return newUserObject;
    }

    public User updateUser(User oldUser, UserFullInfoDTO updatedUserDTO){
        try {
            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                    .setContractId(ContractId.fromString(oldUser.getIdContract()))
                    .setGas(1000000)
                    .setFunction("setActive", new ContractFunctionParameters().addBool(updatedUserDTO.getActive()))
                    .execute(client);

            TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
            System.out.println("Status " + fileReceipt.status);

            oldUser.setIdUserType(updatedUserDTO.getIdUserType());
            oldUser.setName(updatedUserDTO.getName());
            oldUser.setEmail(updatedUserDTO.getEmail());
            oldUser.setPassword(updatedUserDTO.getPassword());
            oldUser.setBirthDate(updatedUserDTO.getBirthDate());
            oldUser.setActive(updatedUserDTO.getActive());
            oldUser.setBalance(updatedUserDTO.getBalance());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return oldUser;
    }
}
