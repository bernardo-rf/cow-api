package cow.starter.Field;
import com.hedera.hashgraph.sdk.*;
import cow.starter.Appointment.models.Appointment;
import cow.starter.Appointment.models.AppointmentDTO;
import cow.starter.Bovine.BovineService;
import cow.starter.Bovine.models.Bovine;
import cow.starter.Bovine.models.BovineDTO;
import cow.starter.Field.models.*;
import cow.starter.User.models.User;
import cow.starter.User.models.UserRepository;
import cow.starter.utilities.EnvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

@Service
public class FieldService {

    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BovineService bovineService;


    public Client client = Client.forTestnet();

    public FieldDTO convertToSimpleDTO(Field field, List<BovineDTO> bovineDTOS) {
        return new FieldDTO(field.getIdField(), field.getIdOwner(), field.getActive(),
                field.getFieldDescription(), field.getIdContract(), field.getLatitude(),
                field.getLongitude(), field.getAddress(), field.getLimit(),
                field.getObservation(), bovineDTOS);
    }

    public FieldFullInfoDTO convertToDTO(Field field, int currentOccupation, int currentOccupationPercentage) {
        return new FieldFullInfoDTO(field.getIdField(), field.getIdOwner(), field.getIdContract(),
                field.getFieldDescription(), field.getAddress(), field.getLatitude(), field.getLongitude(),
                field.getLimit(), currentOccupation, field.getActive(), field.getObservation(),
                currentOccupationPercentage);
    }

    public boolean checkFieldValues(String idOwner){
        User user = userRepository.getUserByIDOwner(idOwner);
        if (user != null) {
                return user.getIdUser() != 0;
            }
        return false;
    }

    public FieldFullInfoDTO getFieldFullInfo(long fieldId){
        Field field = fieldRepository.getField(fieldId);
        if (field != null){
            int fieldsCurrentOccupation = fieldRepository.getFieldCurrentOccupation(field.getIdField());
            int currentOccupationPercentage = (int)(((fieldsCurrentOccupation*1.0)/field.getLimit())*100);
            return convertToDTO(field, fieldsCurrentOccupation, currentOccupationPercentage);
        }
        return new FieldFullInfoDTO();
    }

    public List<FieldFullInfoDTO> getFieldsFullInfo(String idOwner) {
        List<Field> fields = fieldRepository.getAllFieldsByOwner(idOwner);
        if (fields.isEmpty()){
            return new ArrayList<>();
        }

        List<FieldFullInfoDTO> fieldFullInfoDTOList = new ArrayList<>();
        List<Integer> fieldsCurrentOccupation = fieldRepository.getAllFieldsCurrentOccupation();
        if (!fieldsCurrentOccupation.isEmpty()){
            for (int i=0; i <= fields.size() -1; i++){
                Field field = fields.get(i);
                FieldFullInfoDTO fieldFullInfoDTO = getFieldFullInfo(field.getIdField());
                fieldFullInfoDTOList.add(fieldFullInfoDTO);
            }
        }
        return fieldFullInfoDTOList;
    }

    public List<BovineDTO> getBovineDTOList(List<Bovine> bovines){
        List<BovineDTO> bovineDTOList = new ArrayList<>();
        for (Bovine bovine: bovines) {
            BovineDTO bovineDTO = bovineService.convertToDTO(bovine);
            bovineDTOList.add(bovineDTO);
        }
        return bovineDTOList;
    }

    public FieldDTO createField(FieldCreateDTO fieldCreateDTO) {
        FieldDTO fieldDTO = new FieldDTO();
        try {
            Field field = fieldRepository.checkFieldByAddressAndIDOwner(fieldCreateDTO.getAddress(), fieldCreateDTO.getIdOwner());
            if (field != null){
                fieldDTO.setIdField(999999);
                return fieldDTO;
            }
            File myObj = new File("D:\\Bernardo\\PolitecnicoLeiria\\MEI_CM\\2ano\\final_project\\COW.API\\src\\main\\java\\cow\\starter\\Field\\Field.bin");
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
                        System.out.println("Contract Created =" + fileReceipt2);

                        TransactionResponse contractCreateTransaction = new ContractCreateTransaction()
                                .setBytecodeFileId(bytecodeFileId)
                                .setGas(3000000)
                                .setAdminKey(operatorKey)
                                .setConstructorParameters(new ContractFunctionParameters()
                                        .addString(fieldCreateDTO.getFieldDescription())
                                        .addUint256(BigDecimal.valueOf(fieldCreateDTO.getLatitude()).toBigInteger())
                                        .addUint256(BigDecimal.valueOf(fieldCreateDTO.getLongitude()).toBigInteger())
                                        .addBool(fieldCreateDTO.getActive())
                                        .addString(fieldCreateDTO.getObservation())
                                        .addString(fieldCreateDTO.getIdOwner()))
                                .execute(client);

                        TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                        System.out.println("Contract Filled " + fileReceipt3.contractId);

                        ContractId contractId = fileReceipt3.contractId;
                        if (contractId != null) {
                            Field newField = new Field(fileReceipt3.contractId.toString(), fieldCreateDTO.getIdOwner(),
                                    fieldCreateDTO.getFieldDescription(), fieldCreateDTO.getAddress(), fieldCreateDTO.getLimit(),
                                    fieldCreateDTO.getLatitude(), fieldCreateDTO.getLongitude(), fieldCreateDTO.getActive(),
                                    fieldCreateDTO.getObservation());
                            fieldRepository.save(newField);

                            fieldDTO = convertToSimpleDTO(newField, new ArrayList<>());
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
                    client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

                    TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                            .setContractId(ContractId.fromString(fieldDTO.getIdContract()))
                            .setGas(100000)
                            .setFunction("setUpdate", new ContractFunctionParameters()
                                    .addUint256(BigDecimal.valueOf(fieldDTO.getLatitude()).toBigInteger())
                                    .addUint256(BigDecimal.valueOf(fieldDTO.getLongitude()).toBigInteger())
                                    .addBool(fieldDTO.getActive()))
                            .execute(client);

                    TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
                    System.out.println("Status " + fileReceipt.status);

                    Field field = fieldRepository.getField(fieldDTO.getIdField());
                    if (field != null) {
                        field.setFieldDescription(fieldDTO.getFieldDescription());
                        field.setAddress(fieldDTO.getAddress());
                        field.setLimit(fieldDTO.getLimit());
                        field.setLatitude(fieldDTO.getLatitude());
                        field.setLongitude(fieldDTO.getLongitude());
                        field.setActive(fieldDTO.getActive());
                        field.setObservation(fieldDTO.getObservation());
                        fieldRepository.save(field);

                        List<BovineDTO> bovineDTOS = new ArrayList<>();
                        if (!fieldDTO.getBovines().isEmpty()){
                            bovineDTOS = bovineService.updateBovineLocation(fieldDTO.getBovines(),
                                    fieldDTO.getIdField());
                            return convertToSimpleDTO(field, bovineDTOS);
                        }
                        return convertToSimpleDTO(field, bovineDTOS);
                    }
                }
        } catch (TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
        return emptyFieldDTO;
    }

    public Boolean deleteField(long idField){
        try {
            PrivateKey operatorKey = EnvUtils.getOperatorKey();
            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            if (client.getOperatorAccountId() != null){
                Field fieldToDelete = fieldRepository.getField(idField);

                ContractDeleteTransaction transaction = new ContractDeleteTransaction()
                        .setTransferAccountId(client.getOperatorAccountId())
                        .setContractId(ContractId.fromString(fieldToDelete.getIdContract()));

                TransactionResponse txResponse = transaction.freezeWith(client).sign(operatorKey).execute(client);
                TransactionReceipt receipt = txResponse.getReceipt(client);
                System.out.println("STATUS:" + receipt.status);

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