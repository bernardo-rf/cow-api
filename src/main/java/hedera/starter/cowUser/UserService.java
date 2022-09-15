package hedera.starter.cowUser;

import com.hedera.hashgraph.sdk.*;
import hedera.starter.cowUser.models.User;
import hedera.starter.cowUser.models.UserDTO;
import hedera.starter.cowUserType.models.UserType;
import hedera.starter.utilities.EnvUtils;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
public class UserService {
    public Client client = Client.forTestnet();
    private String PATH_USER_BIN = "D:\\Bernardo\\PolitecnicoLeiria\\MEI_CM\\2ano\\final_project\\COW.API\\src\\main\\java\\hedera\\starter\\cowUser\\UserContract.bin";

    public UserDTO convertToDTO(User user, String userType){
        return new UserDTO(user.getIdUser(), user.getIdContract(), user.getIdWallet(), user.getIdUserType(), userType,
                user.getName(), user.getBirthDate(), user.getEmail(), user.getPassword(), user.getActive());
    }

    public UserDTO getUserDTO(Optional<User> userAux, Optional<UserType> userType){
        if(userType.isPresent()){
            return convertToDTO(userAux.get(), userType.get().getDescription().toUpperCase());
        }
        return new UserDTO();
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

                String objectByteCode = myReader.nextLine();
                byte[] bytecode = objectByteCode.getBytes(StandardCharsets.UTF_8);

                TransactionResponse fileCreateTx = new FileCreateTransaction()
                        .setKeys(operatorKey)
                        .freezeWith(client)
                        .execute(client);

                TransactionReceipt fileReceipt1 = fileCreateTx.getReceipt(client);
                FileId bytecodeFileId = fileReceipt1.fileId;

                ContractId newContractId = null;
                AccountId newAccountId = null;
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
                            .setGas(300000)
                            .setAdminKey(operatorKey)
                            .setConstructorParameters(new ContractFunctionParameters().addUint32(idUserType)
                                    .addString(name).addString(birthDate.toString()).addString(email)
                                    .addString(password).addBool(active))
                            .execute(client);

                    TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                    System.out.println("Contract Filled " + fileReceipt3.contractId);

                    newContractId = fileReceipt3.contractId;

                    //ACCOUNT CREATION
                    PrivateKey newAccountPrivateKey = PrivateKey.generateED25519();
                    PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();

                    TransactionResponse newAccount = new AccountCreateTransaction()
                            .setKey(newAccountPublicKey)
                            .setInitialBalance( Hbar.fromTinybars(1000))
                            .execute(client);

                    newAccountId = newAccount.getReceipt(client).accountId;
                }

                if ((newAccountId != null) && (newContractId != null)) {
                    newUserObject.add(newContractId.toString());
                    newUserObject.add(newAccountId.toString());
                }
            }

        } catch (FileNotFoundException | TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }

        return newUserObject;
    }


}
