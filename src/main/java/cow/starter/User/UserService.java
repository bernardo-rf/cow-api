package cow.starter.User;

import com.hedera.hashgraph.sdk.*;
import cow.starter.User.models.*;
import cow.starter.UserType.models.UserType;
import cow.starter.UserType.models.UserTypeRepository;
import cow.starter.utilities.EnvUtils;
import cow.starter.utilities.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

@Service
public class UserService {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public Client client = Client.forTestnet();

    public UserFullInfoDTO convertToDTO(User user, String userType) {
        return new UserFullInfoDTO(user.getIdUser(), user.getIdContract(), user.getIdWallet(), user.getUserType().getIdUserType(),
                userType, user.getName(), user.getBirthDate(), user.getEmail(), user.getPassword(), user.getActive(),
                user.getBalance(), user.getFullName(), user.getImageCID());
    }

    public UserFullInfoDTO getUserFullInfo(long idUser) {
        User user = userRepository.getUser(idUser);
        if (user != null) {
            UserType userType = userTypeRepository.getUserType(user.getUserType().getIdUserType());
            if (userType != null) {
                return convertToDTO(user, userType.getDescription().toUpperCase());
            }
        }
        return new UserFullInfoDTO();
    }


    public UserAuthResponseDTO authenticate(UserAuthDTO userDTO) {
        UserAuthResponseDTO emptyUserAuthResponseDTO = new UserAuthResponseDTO();
        User user = userRepository.getUserByEmail(userDTO.getEmail(), 0);
        if (user != null && bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            if (!user.getActive()) {
                emptyUserAuthResponseDTO.setToken("USER_DISABLE");
                return emptyUserAuthResponseDTO;
            }
            UserAuthResponseDTO userAuthResponseDTO = new UserAuthResponseDTO();
            UserFullInfoDTO userFullInfoDTO = getUserFullInfo(user.getIdUser());
            if (userFullInfoDTO.getIdUser() != 0) {
                userAuthResponseDTO.setToken(jwtToken.generateToken(user));
                userAuthResponseDTO.setUser(userFullInfoDTO);
                return userAuthResponseDTO;
            }
        }
        return emptyUserAuthResponseDTO;
    }

    public UserAuthResponseDTO createUser(UserCreateDTO userCreateDTO) {
        UserAuthResponseDTO emptyUserAuthResponseDTO = new UserAuthResponseDTO();
        try {

            User userAux = userRepository.getUserByEmail(userCreateDTO.getEmail(), 0);
            if (userAux != null) {
                emptyUserAuthResponseDTO.setToken("ERROR_EMAIL");
                return emptyUserAuthResponseDTO;
            }

            UserType userType = userTypeRepository.getUserType(userCreateDTO.getIdUserType());
            if (userType == null) {
                emptyUserAuthResponseDTO.setToken("ERROR_USER_TYPE");
                return emptyUserAuthResponseDTO;
            }

            File myObj = new File(EnvUtils.getProjectPath() + "COW.API\\src\\main\\java\\cow\\starter\\User\\User.bin");
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
                        .setInitialBalance(Hbar.fromTinybars(1000))
                        .execute(client);

                newAccountId = newAccount.getReceipt(client).accountId;

                AccountBalance tokenBalance = null;
                if (newAccountId != null) {
                    AccountBalanceQuery query = new AccountBalanceQuery()
                            .setAccountId(newAccountId);
                    tokenBalance = query.execute(client);
                }

                if (tokenBalance != null) {
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

                        String balanceAux = tokenBalance.hbars.toString();
                        String balance = balanceAux.substring(0, balanceAux.length() - 3);

                        TransactionResponse contractCreateTransaction = new ContractCreateTransaction()
                                .setBytecodeFileId(bytecodeFileId)
                                .setGas(300000)
                                .setAdminKey(operatorKey)
                                .setConstructorParameters(new ContractFunctionParameters()
                                        .addUint256(BigInteger.valueOf(userCreateDTO.getIdUserType()))
                                        .addString(userCreateDTO.getName()).addString("")
                                        .addString(userCreateDTO.getEmail())
                                        .addString(bCryptPasswordEncoder.encode(userCreateDTO.getPassword().toUpperCase()))
                                        .addBool(true)
                                        .addUint256(BigInteger.valueOf(Long.parseLong(balance))))
                                .execute(client);

                        TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                        System.out.println("Contract Filled " + fileReceipt3.contractId);

                        if (fileReceipt3.contractId != null) {
                            String idContract = fileReceipt3.contractId.toString();
                            String idAccount = newAccountId.toString();

                            User user = new User(idContract, idAccount, userType, userCreateDTO.getName(),
                                    null, userCreateDTO.getEmail(),
                                    bCryptPasswordEncoder.encode(userCreateDTO.getPassword()), true,
                                    Double.valueOf(balance), "", "");
                            userRepository.save(user);

                            return new UserAuthResponseDTO( jwtToken.generateToken(user),
                                    convertToDTO(user, userType.getDescription().toUpperCase()));
                        }
                    }
                }
            }
        } catch (FileNotFoundException | TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
        return emptyUserAuthResponseDTO;
    }

    public UserFullInfoDTO updateUser(UserDTO userDTO) {
        UserFullInfoDTO emptyUser = new UserFullInfoDTO();
        try {
            User user = userRepository.getUserByIDWallet(userDTO.getIdWallet());
            if (bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                emptyUser.setName("error_password_equals_to_previous");
                return emptyUser;
            }

            User userAux = userRepository.getUserByEmail(userDTO.getEmail(), userDTO.getIdUser());
            if (userAux != null) {
                emptyUser.setName("error_email_already_taken");
                return emptyUser;
            }

            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                    .setContractId(ContractId.fromString(userDTO.getIdContract()))
                    .setGas(300000)
                    .setFunction("setUpdate", new ContractFunctionParameters()
                            .addUint256(BigInteger.valueOf(userDTO.getIdUserType()))
                            .addUint256(BigInteger.valueOf(userDTO.getBirthDate().getTime()))
                            .addBool(userDTO.getActive()))
                    .execute(client);

            TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
            System.out.println("Status " + fileReceipt.status);

            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setBirthDate(userDTO.getBirthDate());
            user.setFullName(userDTO.getFullName());
            user.setImageCID(userDTO.getImageCID());

            if (!userDTO.getPassword().isEmpty()) {
                user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword().toUpperCase()));
            }
            userRepository.save(user);

            return getUserFullInfo(user.getIdUser());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return emptyUser;
    }
}
