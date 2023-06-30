/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field.boundary;

import static com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaExecutor.*;

import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseByteCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseService;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaReceipt;
import com.bernardo.figueiredo.cow.api.business.bovine.boundary.BovineService;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.field.dto.*;
import com.bernardo.figueiredo.cow.api.business.user.boundary.UserService;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.bernardo.figueiredo.cow.api.utils.EnvUtils;
import com.hedera.hashgraph.sdk.*;
import java.util.*;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldService extends BaseService {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private BaseByteCode baseByteCode;

    @Autowired
    private UserService userService;

    @Autowired
    private BovineService bovineService;

    public Field getFieldById(long id) {
        Field field = fieldRepository.getFieldById(id);

        if (field == null) {
            throw new ErrorCodeException(ErrorCode.FIELD_NOT_FOUND);
        }
        return field;
    }

    public List<Field> getFieldsByUserWalletId(String idWallet) {
        List<Field> fields = fieldRepository.getFieldsByUserWalletId(idWallet);

        if (fields.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.FIELD_NOT_FOUND);
        }

        return fields;
    }

    public List<Field> getFieldsAvailableByUserWalletId(String idWallet) {
        List<Field> fields = fieldRepository.getFieldsByUserWalletId(idWallet);

        if (fields.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.FIELD_NOT_FOUND);
        }

        List<Field> fieldsAvailable = new ArrayList<>();
        for (Field field : fields) {
            List<Bovine> bovines = bovineService.getBovinesAvailableByUserWalletIdAndFieldId(idWallet, field.getId());

            if (bovines.size() == field.getMaxCapacityLimit()) {
                continue;
            }

            fieldsAvailable.add(field);
        }

        if (fieldsAvailable.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.FIELD_NOT_FOUND);
        }
        return fieldsAvailable;
    }

    public Field createField(FieldCreateDTO fieldCreateDTO) {
        Field field;
        HederaReceipt receipt;
        FileId fileId;

        User user = userService.getUserById(fieldCreateDTO.getIdOwner());

        Field checkField = fieldRepository.getFieldAddressByDescriptionAndUserWalletId(
                fieldCreateDTO.getAddress(), fieldCreateDTO.getFieldDescription(), user.getIdWallet());
        if (checkField != null) {
            throw new ErrorCodeException(ErrorCode.FIELD_ADDRESS_INVALID);
        }

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getHederaContractFile(client);

            validateReceiptStatus(receipt);

            fileId = receipt.getFileId();

            receipt = getHederaContractFileAppend(client, fileId, baseByteCode.getFieldByteCode(), 10, 2);

            validateReceiptStatus(receipt);

            Field newField = new Field(
                    user,
                    fieldCreateDTO.getFieldDescription(),
                    fieldCreateDTO.getAddress(),
                    fieldCreateDTO.getMaxCapacityLimit(),
                    fieldCreateDTO.getLatitude(),
                    fieldCreateDTO.getLongitude(),
                    fieldCreateDTO.getActive(),
                    fieldCreateDTO.getObservation());

            receipt = getFieldDeployReceipt(client, fileId, newField);

            validateReceiptStatus(receipt);

            if (receipt.getContractId() == null) {
                throw new ErrorCodeException(ErrorCode.HEDERA_CONTRACT_ID_NOT_FOUND);
            }

            newField.setIdContract(receipt.getContractId().toString());
            field = fieldRepository.save(newField);

            if(fieldCreateDTO.getBovines() != null){
                for (long bovineId : fieldCreateDTO.getBovines()) {
                    Bovine bovine = bovineService.getBovineById(bovineId);
                    if (bovine == null) {
                        throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
                    }

                    bovineService.updateBovineLocation(bovine.getId(), newField.getId());
                }
            }
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.FIELD_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.FIELD_DEPLOY_FAILED));
        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        return field;
    }

    private HederaReceipt getFieldDeployReceipt(Client client, FileId fileId, Field field) throws TimeoutException {
        try {
            return buildFieldDeployReceipt(client, fileId, field);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.FIELD_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.FIELD_DEPLOY_FAILED));
        }
    }

    private HederaReceipt buildFieldDeployReceipt(Client client, FileId byteCodeFileId, Field newField)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractCreateTransaction contractCreateTransaction = new ContractCreateTransaction()
                .setBytecodeFileId(byteCodeFileId)
                .setGas(400_000)
                .setAdminKey(EnvUtils.getOperatorKey())
                .setConstructorParameters(new ContractFunctionParameters()
                        .addString(String.valueOf(newField.getOwner().getId()))
                        .addString(newField.getFieldDescription())
                        .addString(newField.getLatitude().toString())
                        .addString(newField.getLongitude().toString())
                        .addBool(newField.getActive())
                        .addString(newField.getObservation()));

        return execute(client, contractCreateTransaction);
    }

    public Field updateField(long id, FieldDTO fieldDTO) {

        Field updateField = fieldRepository.getFieldById(id);
        if (updateField == null) {
            throw new ErrorCodeException(ErrorCode.FIELD_NOT_FOUND);
        }

        User user = userService.getUserById(fieldDTO.getIdOwner());
        if (user == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        HederaReceipt receipt;

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getFieldUpdateReceipt(client, fieldDTO);

            validateReceiptStatus(receipt);

        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        updateField.setOwner(user);
        updateField.setFieldDescription(fieldDTO.getFieldDescription());
        updateField.setAddress(fieldDTO.getAddress());
        updateField.setMaxCapacityLimit(fieldDTO.getMaxCapacityLimit());
        updateField.setLatitude(fieldDTO.getLatitude());
        updateField.setLongitude(fieldDTO.getLongitude());
        updateField.setActive(fieldDTO.getActive());
        updateField.setObservation(fieldDTO.getObservation());

        if(!updateField.getBovineSet().isEmpty()){
            List<Bovine> bovines =
                    bovineService.getBovinesAvailableByUserWalletIdAndFieldId(user.getIdWallet(), updateField.getId());
            for (Bovine bovine : bovines) {
                bovineService.updateBovineLocation(bovine.getId(), updateField.getId());
            }
        }
        return fieldRepository.save(updateField);
    }

    private HederaReceipt getFieldUpdateReceipt(Client client, FieldDTO fieldDTO) throws TimeoutException {
        try {
            return buildFieldUpdateReceipt(client, fieldDTO);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.FIELD_UPDATE_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.FIELD_UPDATE_FAILED));
        }
    }

    private HederaReceipt buildFieldUpdateReceipt(Client client, FieldDTO fieldDTO)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractExecuteTransaction contractCreateTransaction = new ContractExecuteTransaction()
                .setContractId(ContractId.fromString(fieldDTO.getIdContract()))
                .setGas(300_000)
                .setFunction(
                        "setUpdate",
                        new ContractFunctionParameters()
                                .addString(String.valueOf(fieldDTO.getIdOwner()))
                                .addString(fieldDTO.getFieldDescription())
                                .addString(fieldDTO.getLatitude().toString())
                                .addString(fieldDTO.getLongitude().toString())
                                .addBool(fieldDTO.getActive())
                                .addString(fieldDTO.getObservation()));

        return execute(client, contractCreateTransaction);
    }

    public void deleteField(long id) {
        Field deleteField = fieldRepository.getFieldById(id);
        if (deleteField == null) {
            throw new ErrorCodeException(ErrorCode.FIELD_NOT_FOUND);
        }

        HederaReceipt receipt;

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getFieldDeleteReceipt(client, deleteField.getIdContract());

            validateReceiptStatus(receipt);

        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        fieldRepository.delete(deleteField);
    }

    private HederaReceipt getFieldDeleteReceipt(Client client, String fieldContractId) throws TimeoutException {
        try {
            return buildFieldDeleteReceipt(client, fieldContractId);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.FIELD_DELETE_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.FIELD_DELETE_FAILED));
        }
    }

    private HederaReceipt buildFieldDeleteReceipt(Client client, String fieldContractId)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractDeleteTransaction contractDeleteTransaction = new ContractDeleteTransaction()
                .setTransferAccountId(EnvUtils.getOperatorId())
                .setContractId(ContractId.fromString(fieldContractId));

        return freezeWithSignExecute(client, EnvUtils.getOperatorKey(), contractDeleteTransaction);
    }
}
