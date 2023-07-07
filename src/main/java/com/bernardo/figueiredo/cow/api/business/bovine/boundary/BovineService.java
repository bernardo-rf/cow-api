/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.bovine.boundary;

import static com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaExecutor.*;
import static com.bernardo.figueiredo.cow.api.utils.Constants.MINT_FUNCTION;

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

        validateBovineNull(bovine);

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

    public List<Bovine> getBovinesAvailableByUserWalletIdAndFieldId(String idWallet, long idField) {
        List<Bovine> bovines = bovineRepository.getBovinesAvailableByUserWalletIdAndFieldId(idWallet, idField);

        return validateBovinesEmpty(bovines);
    }

    public List<Bovine> getBovineGenealogyById(long id) {
        List<Bovine> bovines = bovineRepository.getBovineGenealogyById(id);

        return validateBovinesEmpty(bovines);
    }

    public Bovine createBovine(BovineCreateDTO bovineCreateDTO) {
        Bovine mintedBovine;
        HederaReceipt receipt;
        FileId fileId;

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getHederaContractFile(client);

            validateReceiptStatus(receipt);

            fileId = receipt.getFileId();

            receipt = getHederaContractFileAppend(client, fileId, baseByteCode.getBovineByteCode(), 10, 2);

            validateReceiptStatus(receipt);

            receipt = getBovineDeployReceipt(client, fileId);

            validateReceiptStatus(receipt);

            if (receipt.getContractId() == null) {
                throw new ErrorCodeException(ErrorCode.HEDERA_CONTRACT_ID_NOT_FOUND);
            }

            bovineCreateDTO.setIdContract(receipt.getContractId().toString());
            mintedBovine = mintBovine(bovineCreateDTO);

        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.BOVINE_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.BOVINE_DEPLOY_FAILED));
        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        return mintedBovine;
    }

    private void addFieldHistory(Bovine bovine, Field field) {
        FieldHistoryCreatedDTO historyCreatedDTO =
                new FieldHistoryCreatedDTO(field.getId(), bovine.getId(), new Date());
        FieldHistory fieldHistory = fieldHistoryService.createFieldHistory(historyCreatedDTO);

        if (fieldHistory == null) {
            throw new ErrorCodeException(ErrorCode.HISTORY_FIELD_CREATE_FAILED);
        }
    }

    public Bovine mintBovine(BovineCreateDTO bovineCreateDTO) {
        Bovine mintBovine;
        HederaReceipt receipt;

        Bovine checkBovine = bovineRepository.getBovineBySerialNumber(bovineCreateDTO.getSerialNumber());
        if (checkBovine != null) {
            throw new ErrorCodeException(ErrorCode.BOVINE_SERIAL_NUMBER_INVALID);
        }

        User user = userService.getUserById(bovineCreateDTO.getIdOwner());
        Field field = fieldService.getFieldById(bovineCreateDTO.getIdField());

        Bovine newBovine = new Bovine(
                bovineCreateDTO.getIdContract(),
                user,
                field,
                bovineCreateDTO.getSerialNumber(),
                bovineCreateDTO.getBirthDate(),
                bovineCreateDTO.getWeight(),
                bovineCreateDTO.getHeight(),
                bovineCreateDTO.getBreed(),
                bovineCreateDTO.getColor(),
                bovineCreateDTO.isGender(),
                bovineCreateDTO.getActive(),
                bovineCreateDTO.getObservation(),
                bovineCreateDTO.getImageCID());

        if(bovineCreateDTO.getIdBovineParent1() != 0){
            Bovine bovineParent1 = bovineRepository.getBovineById(bovineCreateDTO.getIdBovineParent1());
            validateBovineNull(bovineParent1);
            newBovine.setBovineParent1(bovineParent1);
        }

        if(bovineCreateDTO.getIdBovineParent2() != 0){
            Bovine bovineParent2 = bovineRepository.getBovineById(bovineCreateDTO.getIdBovineParent2());
            validateBovineNull(bovineParent2);
            newBovine.setBovineParent1(bovineParent2);
        }

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getBovineMintReceipt(client, newBovine);

            validateReceiptStatus(receipt);

            if (receipt.getContractId() == null) {
                throw new ErrorCodeException(ErrorCode.HEDERA_CONTRACT_ID_NOT_FOUND);
            }

            mintBovine = bovineRepository.save(newBovine);

            addFieldHistory(mintBovine, field);

        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        return mintBovine;
    }

    private HederaReceipt getBovineDeployReceipt(Client client, FileId fileId) throws TimeoutException {
        try {
            return buildBovineDeployReceipt(client, fileId);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.BOVINE_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.BOVINE_DEPLOY_FAILED));
        }
    }

    private HederaReceipt buildBovineDeployReceipt(Client client, FileId byteCodeFileId)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractCreateTransaction contractCreateTransaction = new ContractCreateTransaction()
                .setBytecodeFileId(byteCodeFileId)
                .setGas(400_000)
                .setAdminKey(EnvUtils.getOperatorKey());

        return execute(client, contractCreateTransaction);
    }

    private HederaReceipt getBovineMintReceipt(Client client, Bovine bovine) throws TimeoutException {
        try {
            return buildBovineMintReceipt(client, bovine);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.BOVINE_MINT_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.BOVINE_MINT_FAILED));
        }
    }

    private HederaReceipt buildBovineMintReceipt(Client client, Bovine newBovine)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        long idBovineParent1 = 0;
        if(newBovine.getBovineParent1() != null) {
            idBovineParent1 = newBovine.getBovineParent1().getId();
        }

        long idBovineParent2 = 0;
        if(newBovine.getBovineParent2() != null) {
            idBovineParent2 = newBovine.getBovineParent2().getId();
        }

        ContractExecuteTransaction contractExecuteTransaction = new ContractExecuteTransaction()
                .setContractId(ContractId.fromString(newBovine.getIdContract()))
                .setGas(400_000)
                .setFunction(
                        MINT_FUNCTION,
                        new ContractFunctionParameters()
                                .addUint256(
                                        (BigInteger.valueOf(newBovine.getOwner().getId())))
                                .addUint256(
                                        BigInteger.valueOf(newBovine.getField().getId()))
                                .addUint256(BigInteger.valueOf(newBovine.getSerialNumber()))
                                .addUint256(BigInteger.valueOf(
                                        Date.from(newBovine.getBirthDate()).getTime()))
                                .addBool(newBovine.getActive())
                                .addString(newBovine.getObservation())
                                .addUint256(BigInteger.valueOf(idBovineParent1))
                                .addUint256(BigInteger.valueOf(idBovineParent2))
                                .addBool(newBovine.isGender()));

        return execute(client, contractExecuteTransaction);
    }

    public Bovine updateBovine(long id, BovineDTO bovineDTO) {
        Bovine updateBovine = bovineRepository.getBovineById(id);
        validateBovineNull(updateBovine);

        User user = userService.getUserById(bovineDTO.getIdOwner());
        if (user == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        if(bovineDTO.getIdBovineParent1() != 0){
            Bovine bovineParent1 = bovineRepository.getBovineById(bovineDTO.getIdBovineParent1());
            validateBovineNull(bovineParent1);
            updateBovine.setBovineParent1(bovineParent1);
        }

        if(bovineDTO.getIdBovineParent2() != 0){
            Bovine bovineParent2 = bovineRepository.getBovineById(bovineDTO.getIdBovineParent2());
            validateBovineNull(bovineParent2);
            updateBovine.setBovineParent2(bovineParent2);
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

        addFieldHistory(updateBovine, field);

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

        long idBovineParent1 = 0;
        if(bovineDTO.getIdBovineParent1() != 0) {
            idBovineParent1 = bovineDTO.getIdBovineParent1();
        }

        long idBovineParent2 = 0;
        if(bovineDTO.getIdBovineParent2() != 0) {
            idBovineParent2 = bovineDTO.getIdBovineParent2();
        }

        ContractExecuteTransaction contractCreateTransaction = new ContractExecuteTransaction()
                .setContractId(ContractId.fromString(bovineDTO.getIdContract()))
                .setGas(250_000)
                .setFunction(
                        "setUpdate",
                        new ContractFunctionParameters()
                                .addUint256((BigInteger.valueOf(bovineDTO.getIdToken())))
                                .addUint256((BigInteger.valueOf(bovineDTO.getIdOwner())))
                                .addUint256(BigInteger.valueOf(bovineDTO.getIdField()))
                                .addUint256(BigInteger.valueOf(bovineDTO.getSerialNumber()))
                                .addUint256(BigInteger.valueOf(
                                        Date.from(bovineDTO.getBirthDate()).getTime()))
                                .addBool(bovineDTO.getActive())
                                .addString(bovineDTO.getObservation())
                                .addUint256(BigInteger.valueOf(idBovineParent1))
                                .addUint256(BigInteger.valueOf(idBovineParent2))
                                .addBool(bovineDTO.isGender()));

        return execute(client, contractCreateTransaction);
    }

    public Bovine updateBovineLocation(long id, long idField) {
        Bovine updateBovine = bovineRepository.getBovineById(id);
        validateBovineNull(updateBovine);

        Field field = fieldService.getFieldById(idField);

        updateBovine.setField(field);
        Bovine bovine = bovineRepository.save(updateBovine);
        validateBovineNull(updateBovine);

        addFieldHistory(bovine, field);

        return bovine;
    }

    public void deleteBovine(long id) {
        Bovine deleteBovine = bovineRepository.getBovineById(id);
        validateBovineNull(deleteBovine);

        HederaReceipt receipt;

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getBovineDeleteReceipt(client, deleteBovine.getIdContract());

            validateReceiptStatus(receipt);

        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        bovineRepository.delete(deleteBovine);
    }

    private HederaReceipt getBovineDeleteReceipt(Client client, String bovineContractId) throws TimeoutException {
        try {
            return buildBovineDeleteReceipt(client, bovineContractId);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.BOVINE_DELETE_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.BOVINE_DELETE_FAILED));
        }
    }

    private HederaReceipt buildBovineDeleteReceipt(Client client, String bovineContractId)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractDeleteTransaction contractDeleteTransaction = new ContractDeleteTransaction()
                .setTransferAccountId(EnvUtils.getOperatorId())
                .setContractId(ContractId.fromString(bovineContractId));

        return freezeWithSignExecute(client, EnvUtils.getOperatorKey(), contractDeleteTransaction);
    }

    private static void validateBovineNull(Bovine bovine) {
        if (bovine == null) {
            throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
        }
    }

    @NotNull private static List<Bovine> validateBovinesEmpty(List<Bovine> bovines) {
        if (bovines.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
        }

        return bovines;
    }
}
