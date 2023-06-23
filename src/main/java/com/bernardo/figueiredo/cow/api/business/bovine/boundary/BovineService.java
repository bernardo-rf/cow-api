/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.bovine.boundary;

import static com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaExecutor.*;

import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseByteCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseService;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaReceipt;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.*;
import com.bernardo.figueiredo.cow.api.business.field.boundary.FieldService;
import com.bernardo.figueiredo.cow.api.business.field.dto.Field;
import com.bernardo.figueiredo.cow.api.business.field_history.boundary.FieldHistoryService;
import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistory;
import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistoryCreatedDTO;
import com.bernardo.figueiredo.cow.api.business.user.boundary.UserService;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.bernardo.figueiredo.cow.api.utils.EnvUtils;
import com.hedera.hashgraph.sdk.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeoutException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BovineService extends BaseService {

    @Autowired
    BovineRepository bovineRepository;

    @Autowired
    BaseByteCode baseByteCode;

    @Autowired
    UserService userService;

    @Autowired
    FieldService fieldService;

    @Autowired
    FieldHistoryService fieldHistoryService;

    public Bovine getBovineById(long id) {
        Bovine bovine = bovineRepository.getBovineById(id);

        if (bovine == null) {
            throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
        }

        return bovine;
    }

    public List<Bovine> getBovinesActive() {
        List<Bovine> bovines = bovineRepository.getBovinesActive();

        return validateBovinesEmpty(bovines);
    }

    public List<Bovine> getBovinesByUserWalletId(String ownerId) {
        List<Bovine> bovines = bovineRepository.getBovinesByUserWalletId(ownerId);

        return validateBovinesEmpty(bovines);
    }

    public List<Bovine> getBovineToAuctionByUserWalletId(String ownerId) {
        List<Bovine> bovines = bovineRepository.getBovineToAuctionByUserWalletId(ownerId);

        return validateBovinesEmpty(bovines);
    }

    @NotNull private static List<Bovine> validateBovinesEmpty(List<Bovine> bovines) {
        if (bovines.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
        }

        return bovines;
    }

    public List<Bovine> getBovinesAvailableByUserWalletId(long idField, String idWallet) {
        List<Bovine> bovines = bovineRepository.getBovinesAvailableByUserWalletId(idField, idWallet);

        return validateBovinesEmpty(bovines);
    }

    public List<Bovine> getBovineGenealogyById(long id) {
        List<Bovine> bovines = bovineRepository.getBovineGenealogyById(id);

        return validateBovinesEmpty(bovines);
    }

    public Bovine createBovine(BovineCreateDTO bovineCreateDTO) {

        Bovine bovine;
        HederaReceipt receipt;
        FileId fileId;

        Bovine checkBovine = bovineRepository.getBovineBySerialNumber(bovineCreateDTO.getSerialNumber());
        if (checkBovine != null) {
            throw new ErrorCodeException(ErrorCode.BOVINE_SERIAL_NUMBER_INVALID);
        }

        User user = userService.getUserById(bovineCreateDTO.getIdOwner());
        Field field = fieldService.getFieldById(bovineCreateDTO.getIdField());
        Bovine bovineParent1 = bovineRepository.getBovineById(bovineCreateDTO.getIdBovineParent1());
        Bovine bovineParent2 = bovineRepository.getBovineById(bovineCreateDTO.getIdBovineParent2());

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getHederaContractFile(client);

            validateReceiptStatus(receipt);

            fileId = receipt.getFileId();

            receipt = getHederaContractFileAppend(client, fileId, baseByteCode.getFieldByteCode(), 10, 2);

            validateReceiptStatus(receipt);

            Bovine newBovine = new Bovine();
            newBovine.setOwner(user);
            newBovine.setField(field);
            newBovine.setBovineParent1(bovineParent1);
            newBovine.setBovineParent2(bovineParent2);
            newBovine.setGender(bovineCreateDTO.isGender());
            newBovine.setSerialNumber(bovineCreateDTO.getSerialNumber());
            newBovine.setBirthDate(bovineCreateDTO.getBirthDate());
            newBovine.setWeight(bovineCreateDTO.getWeight());
            newBovine.setHeight(bovineCreateDTO.getHeight());
            newBovine.setBreed(bovineCreateDTO.getBreed());
            newBovine.setColor(bovineCreateDTO.getColor());
            newBovine.setActive(bovineCreateDTO.getActive());
            newBovine.setObservation(bovineCreateDTO.getObservation());
            newBovine.setImageCID(bovineCreateDTO.getImageCID());

            receipt = getBovineDeployReceipt(client, fileId, newBovine);

            validateReceiptStatus(receipt);

            if (receipt.getContractId() == null) {
                throw new ErrorCodeException(ErrorCode.HEDERA_CONTRACT_ID_NOT_FOUND);
            }

            newBovine.setIdContract(receipt.getContractId().toString());
            bovine = bovineRepository.save(newBovine);

            FieldHistoryCreatedDTO historyCreatedDTO =
                    new FieldHistoryCreatedDTO(field.getIdField(), bovine.getId(), new Date());
            FieldHistory fieldHistory = fieldHistoryService.createFieldHistory(historyCreatedDTO);

            if (fieldHistory == null) {
                throw new ErrorCodeException(ErrorCode.HISTORY_FIELD_CREATE_FAILED);
            }

        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.BOVINE_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.BOVINE_DEPLOY_FAILED));
        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        return bovine;
    }

    private HederaReceipt getBovineDeployReceipt(Client client, FileId fileId, Bovine bovine) throws TimeoutException {
        try {
            return buildBovineDeployReceipt(client, fileId, bovine);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.BOVINE_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.BOVINE_DEPLOY_FAILED));
        }
    }

    private HederaReceipt buildBovineDeployReceipt(Client client, FileId byteCodeFileId, Bovine newBovine)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractCreateTransaction contractCreateTransaction = new ContractCreateTransaction()
                .setBytecodeFileId(byteCodeFileId)
                .setGas(400_000)
                .setAdminKey(EnvUtils.getOperatorKey())
                .setConstructorParameters(new ContractFunctionParameters()
                        .addString(newBovine.getOwner().getIdContract())
                        .addUint256(BigInteger.valueOf(newBovine.getField().getIdField()))
                        .addUint256(BigInteger.valueOf(newBovine.getSerialNumber()))
                        .addUint256(BigInteger.valueOf(newBovine.getBirthDate().getTime()))
                        .addBool(newBovine.getActive())
                        .addString(newBovine.getObservation())
                        .addUint256(
                                BigInteger.valueOf(newBovine.getBovineParent1().getId()))
                        .addUint256(
                                BigInteger.valueOf(newBovine.getBovineParent2().getId()))
                        .addBool(newBovine.isGender()));

        return execute(client, contractCreateTransaction);
    }

    public Bovine updateBovine(long id, BovineDTO bovineDTO) {
        Bovine updateBovine = bovineRepository.getBovineById(id);
        if (updateBovine == null) {
            throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
        }

        User user = userService.getUserById(bovineDTO.getIdOwner());
        if (user == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        Bovine bovineParent1 = bovineRepository.getBovineById(bovineDTO.getIdBovineParent1());
        if (bovineParent1 == null) {
            throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
        }

        Bovine bovineParent2 = bovineRepository.getBovineById(bovineDTO.getIdBovineParent2());
        if (bovineParent2 == null) {
            throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
        }

        Field field = fieldService.getFieldById(bovineDTO.getIdField());
        if (field == null) {
            throw new ErrorCodeException(ErrorCode.FIELD_NOT_FOUND);
        }

        HederaReceipt receipt;

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getBovineUpdateReceipt(client, bovineDTO);

            validateReceiptStatus(receipt);

        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        updateBovine.setOwner(user);
        updateBovine.setField(field);
        updateBovine.setBovineParent1(bovineParent1);
        updateBovine.setBovineParent2(bovineParent2);
        updateBovine.setGender(bovineDTO.isGender());
        updateBovine.setSerialNumber(bovineDTO.getSerialNumber());
        updateBovine.setBirthDate(bovineDTO.getBirthDate());
        updateBovine.setWeight(bovineDTO.getWeight());
        updateBovine.setHeight(bovineDTO.getHeight());
        updateBovine.setBreed(bovineDTO.getBreed());
        updateBovine.setColor(bovineDTO.getColor());
        updateBovine.setActive(bovineDTO.getActive());
        updateBovine.setObservation(bovineDTO.getObservation());
        updateBovine.setImageCID(bovineDTO.getImageCID());
        bovineRepository.save(updateBovine);

        FieldHistoryCreatedDTO historyCreatedDTO =
                new FieldHistoryCreatedDTO(field.getIdField(), updateBovine.getId(), new Date());
        FieldHistory fieldHistory = fieldHistoryService.createFieldHistory(historyCreatedDTO);

        if (fieldHistory == null) {
            throw new ErrorCodeException(ErrorCode.HISTORY_FIELD_CREATE_FAILED);
        }

        return bovineRepository.save(updateBovine);
    }

    private HederaReceipt getBovineUpdateReceipt(Client client, BovineDTO bovineDTO) throws TimeoutException {
        try {
            return buildBovineUpdateReceipt(client, bovineDTO);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.BOVINE_UPDATE_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.BOVINE_UPDATE_FAILED));
        }
    }

    private HederaReceipt buildBovineUpdateReceipt(Client client, BovineDTO bovineDTO)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractExecuteTransaction contractCreateTransaction = new ContractExecuteTransaction()
                .setContractId(ContractId.fromString(bovineDTO.getIdContract()))
                .setGas(300_000)
                .setFunction(
                        "setUpdate",
                        new ContractFunctionParameters()
                                .addUint256(BigInteger.valueOf(bovineDTO.getIdField()))
                                .addUint256(BigInteger.valueOf(bovineDTO.getSerialNumber()))
                                .addUint256(BigInteger.valueOf(
                                        bovineDTO.getBirthDate().getTime()))
                                .addBool(bovineDTO.getActive())
                                .addUint256(BigInteger.valueOf(bovineDTO.getIdBovineParent1()))
                                .addUint256(BigInteger.valueOf(bovineDTO.getIdBovineParent2()))
                                .addBool(bovineDTO.isGender()));

        return execute(client, contractCreateTransaction);
    }

    // TODO
    //    public Set<BovineDTO> updateBovineLocation(Set<BovineDTO> bovineDTOS, long idField) {
    //        for (BovineDTO bovineDTO : bovineDTOS) {
    //            Bovine bovine = bovineRepository.getBovine(bovineDTO.getIdBovine());
    //            if (bovine != null) {
    //                bovine.setField(fieldRepository.getField(idField));
    //                bovineRepository.save(bovine);
    //
    //                FieldHistoryCreatedDTO historyCreatedDTO =
    //                        new FieldHistoryCreatedDTO(idField, bovineDTO.getIdBovine(), new Date());
    //                FieldHistoryDTO fieldHistoryDTO = fieldHistoryService.createFieldHistory(historyCreatedDTO);
    //            }
    //        }
    //        return bovineDTOS;
    //    }

    // TODO
    //    public boolean deleteBovine(long idBovine) {
    //        try {
    //            PrivateKey operatorKey = EnvUtils.getOperatorKey();
    //            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());
    //
    //            if (client.getOperatorAccountId() != null) {
    //                Bovine bovineToDelete = bovineRepository.getBovine(idBovine);
    //
    //                if (bovineToDelete != null) {
    //                    ContractDeleteTransaction transaction = new ContractDeleteTransaction()
    //                            .setTransferAccountId(client.getOperatorAccountId())
    //                            .setContractId(ContractId.fromString(bovineToDelete.getIdContract()));
    //
    //                    TransactionResponse txResponse =
    //                            transaction.freezeWith(client).sign(operatorKey).execute(client);
    //                    TransactionReceipt receipt = txResponse.getReceipt(client);
    //                    Logger.getLogger("STATUS:" + receipt.status);
    //
    //                    bovineRepository.delete(bovineToDelete);
    //                    return true;
    //                }
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return false;
    //    }
}
