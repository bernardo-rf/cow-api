/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user.boundary;

import static com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaExecutor.*;
import static com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaExecutor.execute;
import static com.bernardo.figueiredo.cow.api.utils.Constants.INITIAL_BALANCE;

import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseByteCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseService;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaReceipt;
import com.bernardo.figueiredo.cow.api.business.user.dto.*;
import com.bernardo.figueiredo.cow.api.business.user_type.boundary.UserTypeService;
import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserType;
import com.bernardo.figueiredo.cow.api.utils.EnvUtils;
import com.bernardo.figueiredo.cow.api.utils.JwtToken;
import com.hedera.hashgraph.sdk.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private BaseByteCode baseByteCode;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User getUserById(long id) {
        User user = userRepository.getUserById(id);

        if (user == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        return user;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);

        if (user == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        return user;
    }

    public boolean checkUserExistsByEmail(String email) {
        User user = userRepository.getUserByEmail(email);

        return user != null;
    }

    public User getUserByWalletId(String idWallet) {
        User user = userRepository.getUserByWalletId(idWallet);

        if (user == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        return user;
    }

    public List<User> getUsersByUserTypeId(long idUserType) {
        List<User> users = userRepository.getUsersByUserTypeId(idUserType);

        if (users.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        return users;
    }

    public UserAuth authenticateUser(UserCredentialDTO userDTO) {
        User user = userRepository.getUserByEmail(userDTO.getEmail());
        if (user == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        if (bCryptPasswordEncoder.matches(user.getPassword(), userDTO.getPassword())) {
            throw new ErrorCodeException(ErrorCode.AUTHENTICATION_FAILED);
        }

        String accessToken = jwtToken.generateToken(user);
        if (accessToken.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.AUTHENTICATION_CREATE_FAILED);
        }

        return new UserAuth(accessToken, user);
    }

    public UserAuth createUser(UserCreateDTO userCreateDTO) {
        User userSaved;
        HederaReceipt receipt;
        FileId fileId;
        AccountId accountId;

        if (checkUserExistsByEmail(userCreateDTO.getEmail())) {
            throw new ErrorCodeException(ErrorCode.USER_EMAIL_INVALID);
        }

        UserType checkUserType = userTypeService.getUserTypeById(userCreateDTO.getIdUserType());
        if (checkUserType == null) {
            throw new ErrorCodeException(ErrorCode.USER_TYPE_NOT_FOUND);
        }

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            PrivateKey newPrivateKey = PrivateKey.generateED25519();
            PublicKey newPublicKey = newPrivateKey.getPublicKey();

            receipt = getUserAccountReceipt(client, newPublicKey);

            validateReceiptStatus(receipt);

            if (receipt.getAccountId() == null) {
                throw new ErrorCodeException(ErrorCode.HEDERA_ACCOUNT_ID_NOT_FOUND);
            }

            accountId = receipt.getAccountId();

            receipt = getHederaContractFile(client);

            validateReceiptStatus(receipt);

            if (receipt.getFileId() == null) {
                throw new ErrorCodeException(ErrorCode.HEDERA_FIELD_ID_NOT_FOUND);
            }
            fileId = receipt.getFileId();

            receipt = getHederaContractFileAppend(client, fileId, baseByteCode.getUserByteCode(), 10, 2);

            validateReceiptStatus(receipt);

            User newUser = new User(
                    accountId.toString(),
                    checkUserType,
                    userCreateDTO.getName(),
                    userCreateDTO.getBirthDate(),
                    userCreateDTO.getEmail(),
                    bCryptPasswordEncoder.encode(userCreateDTO.getPassword()),
                    true,
                    INITIAL_BALANCE,
                    "",
                    "");

            receipt = getUserDeployReceipt(client, fileId, newUser);

            validateReceiptStatus(receipt);

            if (receipt.getContractId() == null) {
                throw new ErrorCodeException(ErrorCode.HEDERA_CONTRACT_ID_NOT_FOUND);
            }

            newUser.setIdContract(receipt.getContractId().toString());
            userSaved = userRepository.save(newUser);

        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.USER_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.USER_DEPLOY_FAILED));
        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        String accessToken = jwtToken.generateToken(userSaved);
        if (accessToken.isBlank()) {
            throw new ErrorCodeException(ErrorCode.ACCESS_TOKEN_CREATE_FAILED);
        }

        return new UserAuth(accessToken, userSaved);
    }

    private HederaReceipt getUserDeployReceipt(Client client, FileId fileId, User user) throws TimeoutException {
        try {
            return buildUserDeployReceipt(client, fileId, user);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.USER_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.USER_DEPLOY_FAILED));
        }
    }

    private HederaReceipt buildUserDeployReceipt(Client client, FileId byteCodeFileId, User newUser)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractCreateTransaction contractCreateTransaction = new ContractCreateTransaction()
                .setBytecodeFileId(byteCodeFileId)
                .setGas(400_000)
                .setAdminKey(EnvUtils.getOperatorKey())
                .setConstructorParameters(new ContractFunctionParameters()
                        .addUint256(BigInteger.valueOf(newUser.getUserType().getId()))
                        .addString(newUser.getName())
                        .addUint256(BigInteger.valueOf(Date.from(newUser.getBirthDate()).getTime()))
                        .addString(newUser.getEmail())
                        .addBool(newUser.getActive()));

        return execute(client, contractCreateTransaction);
    }

    private HederaReceipt getUserAccountReceipt(Client client, PublicKey publicKey) throws TimeoutException {
        try {
            return buildUserAccountReceipt(client, publicKey);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.USER_ACCOUNT_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.USER_ACCOUNT_FAILED));
        }
    }

    private HederaReceipt buildUserAccountReceipt(Client client, PublicKey publicKey)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        AccountCreateTransaction accountCreateTransaction =
                new AccountCreateTransaction().setKey(publicKey).setInitialBalance(Hbar.from(1_000));

        return execute(client, accountCreateTransaction);
    }

    public User updateUser(long id, UserDTO userDTO) {
        User updateUser = userRepository.getUserById(id);
        if (updateUser == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        User checkUser = userRepository.getUserByEmail(userDTO.getEmail());
        if (checkUser != null) {
            throw new ErrorCodeException(ErrorCode.USER_EMAIL_INVALID);
        }

        if (bCryptPasswordEncoder.matches(updateUser.getPassword(), userDTO.getPassword())) {
            throw new ErrorCodeException(ErrorCode.USER_PASSWORD_INVALID);
        }

        HederaReceipt receipt;

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getUserUpdateReceipt(client, userDTO);

            validateReceiptStatus(receipt);

        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        updateUser.setName(userDTO.getName());
        updateUser.setEmail(userDTO.getEmail());
        updateUser.setBirthDate(userDTO.getBirthDate());
        updateUser.setFullName(userDTO.getFullName());
        updateUser.setImageCID(userDTO.getImageCID());

        return userRepository.save(updateUser);
    }

    private HederaReceipt getUserUpdateReceipt(Client client, UserDTO userDTO) throws TimeoutException {
        try {
            return buildFieldUpdateReceipt(client, userDTO);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.USER_UPDATE_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.USER_UPDATE_FAILED));
        }
    }

    private HederaReceipt buildFieldUpdateReceipt(Client client, UserDTO userDTO)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractExecuteTransaction contractExecuteTransaction = new ContractExecuteTransaction()
                .setContractId(ContractId.fromString(userDTO.getIdContract()))
                .setGas(300_000)
                .setFunction(
                        "setUpdate",
                        new ContractFunctionParameters()
                                .addUint256(BigInteger.valueOf(userDTO.getIdUserType()))
                                .addString(userDTO.getName())
                                .addUint256(BigInteger.valueOf(Date.from(userDTO.getBirthDate()).getTime()))
                                .addString(userDTO.getEmail())
                                .addBool(userDTO.getActive()));

        return execute(client, contractExecuteTransaction);
    }
}
