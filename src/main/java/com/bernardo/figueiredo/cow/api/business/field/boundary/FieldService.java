/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field.boundary;

import com.bernardo.figueiredo.cow.api.business.bovine.boundary.BovineService;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.BovineDTO;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.BovineFullInfoDTO;
import com.bernardo.figueiredo.cow.api.business.field.dto.*;
import com.bernardo.figueiredo.cow.api.business.user.boundary.UserRepository;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.bernardo.figueiredo.cow.api.utils.EnvUtils;
import com.hedera.hashgraph.sdk.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldService {

    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BovineService bovineService;

    public Client client = Client.forTestnet();

    public FieldDTO convertToDTO(Field field, Set<BovineDTO> bovines) {
        return new FieldDTO(
                field.getIdField(),
                field.getUser().getIdWallet(),
                field.getIdContract(),
                field.getFieldDescription(),
                field.getLatitude(),
                field.getLongitude(),
                field.getAddress(),
                field.getFieldLimit(),
                field.getActive(),
                field.getObservation(),
                bovines);
    }

    public FieldFullInfoDTO convertFullInfoToDTO(Field field) {
        int fieldCurrentOccupation = field.getBovineSet().size();
        int currentOccupationPercentage = (int) (((fieldCurrentOccupation * 1.0) / field.getFieldLimit()) * 100);

        return new FieldFullInfoDTO(
                field.getIdField(),
                field.getUser(),
                field.getIdContract(),
                field.getFieldDescription(),
                field.getLatitude(),
                field.getLongitude(),
                field.getAddress(),
                field.getFieldLimit(),
                field.getActive(),
                field.getObservation(),
                fieldCurrentOccupation,
                currentOccupationPercentage);
    }

    public boolean checkFieldValues(String idOwner) {
        User user = userRepository.getUserByIDOwner(idOwner);
        if (user != null) {
            return user.getIdUser() != 0;
        }
        return false;
    }

    public FieldFullInfoDTO getField(long fieldId) {
        Field field = fieldRepository.getField(fieldId);
        if (field == null) {
            return new FieldFullInfoDTO();
        }
        return convertFullInfoToDTO(field);
    }

    public List<FieldFullInfoDTO> getFieldsByIDOwner(String idOwner) {
        List<FieldFullInfoDTO> fieldsList = new ArrayList<>();
        List<Field> fields = fieldRepository.getFieldsByIDOwner(idOwner);
        if (fields.isEmpty()) {
            return fieldsList;
        }

        for (Field field : fields) {
            fieldsList.add(convertFullInfoToDTO(field));
        }
        return fieldsList;
    }

    public List<FieldFullInfoDTO> getFieldsNotOccupied(String idOwner) {
        List<FieldFullInfoDTO> fieldsList = new ArrayList<>();
        List<Field> fields = fieldRepository.getFieldsByIDOwner(idOwner);
        if (fields.isEmpty()) {
            return fieldsList;
        }

        for (Field field : fields) {
            if (field.getBovineSet().size() != field.getFieldLimit()) {
                fieldsList.add(convertFullInfoToDTO(field));
            }
        }
        return fieldsList;
    }

    public FieldDTO getFieldBovines(long idField) {
        FieldDTO fieldDTO = new FieldDTO();
        Field field = fieldRepository.getField(idField);
        if (field == null) {
            return fieldDTO;
        }

        Set<BovineDTO> bovineDTOS = new HashSet<>();
        for (Bovine bovine : field.getBovineSet()) {
            bovineDTOS.add(bovineService.convertToDTO(bovine));
        }

        return convertToDTO(field, bovineDTOS);
    }

    public List<BovineFullInfoDTO> getFieldBovinesNotIn(long idField) {
        Field field = fieldRepository.getField(idField);
        if (field == null) {
            return new ArrayList<>();
        }
        return bovineService.getAllBovineNotInField(idField, field.getUser().getIdWallet());
    }

    public FieldDTO createField(FieldCreateDTO fieldCreateDTO) {
        FieldDTO fieldDTO = new FieldDTO();
        try {
            Field field = fieldRepository.checkFieldByAddressAndIDOwner(
                    fieldCreateDTO.getAddress(), fieldCreateDTO.getIdOwner());
            if (field != null) {
                fieldDTO.setIdField(999999);
                return fieldDTO;
            }
            File myObj = new File(EnvUtils.getProjectPath() + "field/Field.bin");
            Scanner myReader = new Scanner(myObj);

            if (checkFieldValues(fieldCreateDTO.getIdOwner())) {
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

                    if (bytecodeFileId != null) {
                        TransactionResponse fileAppendTransaction = new FileAppendTransaction()
                                .setFileId(bytecodeFileId)
                                .setContents(bytecode)
                                .setMaxChunks(10)
                                .setMaxTransactionFee(new Hbar(2))
                                .execute(client);
                        TransactionReceipt fileReceipt2 = fileAppendTransaction.getReceipt(client);
                        Logger.getLogger("Contract Created =" + fileReceipt2);

                        TransactionResponse contractCreateTransaction = new ContractCreateTransaction()
                                .setBytecodeFileId(bytecodeFileId)
                                .setGas(3000000)
                                .setAdminKey(operatorKey)
                                .setConstructorParameters(new ContractFunctionParameters()
                                        .addString(fieldCreateDTO.getFieldDescription())
                                        .addString(fieldCreateDTO.getLatitude().toString())
                                        .addString(fieldCreateDTO.getLongitude().toString())
                                        .addBool(fieldCreateDTO.getActive())
                                        .addString(fieldCreateDTO.getObservation())
                                        .addString(fieldCreateDTO.getIdOwner()))
                                .execute(client);

                        TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                        Logger.getLogger("Contract Filled " + fileReceipt3.contractId);

                        ContractId contractId = fileReceipt3.contractId;
                        if (contractId != null) {
                            Field newField = new Field(
                                    fileReceipt3.contractId.toString(),
                                    userRepository.getUserByIDOwner(fieldCreateDTO.getIdOwner()),
                                    fieldCreateDTO.getFieldDescription(),
                                    fieldCreateDTO.getAddress(),
                                    fieldCreateDTO.getLimit(),
                                    fieldCreateDTO.getLatitude(),
                                    fieldCreateDTO.getLongitude(),
                                    fieldCreateDTO.getActive(),
                                    fieldCreateDTO.getObservation());
                            fieldRepository.save(newField);

                            fieldDTO = convertToDTO(newField, new HashSet<>());
                            if (fieldDTO.getIdField() != 0) {
                                bovineService.updateBovineLocation(fieldCreateDTO.getBovines(), newField.getIdField());
                                return fieldDTO;
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException | TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
        return fieldDTO;
    }

    public FieldDTO updateField(FieldDTO fieldDTO) {
        FieldDTO emptyFieldDTO = new FieldDTO();
        try {
            if (checkFieldValues(fieldDTO.getIdOwner())) {
                Field field = fieldRepository.getField(fieldDTO.getIdField());
                if (field != null) {
                    client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

                    TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                            .setContractId(ContractId.fromString(field.getIdContract()))
                            .setGas(1000000)
                            .setFunction("setUpdate", new ContractFunctionParameters().addBool(fieldDTO.getActive()))
                            .execute(client);

                    TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
                    Logger.getLogger("Status " + fileReceipt.status);

                    field.setFieldDescription(fieldDTO.getFieldDescription());
                    field.setAddress(fieldDTO.getAddress());
                    field.setFieldLimit(fieldDTO.getLimit());
                    field.setLatitude(fieldDTO.getLatitude());
                    field.setLongitude(fieldDTO.getLongitude());
                    field.setActive(fieldDTO.getActive());
                    field.setObservation(fieldDTO.getObservation());
                    fieldRepository.save(field);

                    Set<BovineDTO> bovineDTOS = new HashSet<>();
                    if (!fieldDTO.getBovines().isEmpty()) {
                        bovineDTOS = bovineService.updateBovineLocation(fieldDTO.getBovines(), fieldDTO.getIdField());
                        return convertToDTO(field, bovineDTOS);
                    }
                    return convertToDTO(field, bovineDTOS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emptyFieldDTO;
    }

    public Boolean deleteField(long idField) {
        try {
            PrivateKey operatorKey = EnvUtils.getOperatorKey();
            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            if (client.getOperatorAccountId() != null) {
                Field fieldToDelete = fieldRepository.getField(idField);

                ContractDeleteTransaction transaction = new ContractDeleteTransaction()
                        .setTransferAccountId(client.getOperatorAccountId())
                        .setContractId(ContractId.fromString(fieldToDelete.getIdContract()));

                TransactionResponse txResponse =
                        transaction.freezeWith(client).sign(operatorKey).execute(client);
                TransactionReceipt receipt = txResponse.getReceipt(client);
                Logger.getLogger("STATUS:" + receipt.status);

                if (fieldToDelete.getIdField() != 0) {
                    fieldRepository.delete(fieldToDelete);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
