package hedera.starter.cowField;
import com.hedera.hashgraph.sdk.*;
import hedera.starter.cowField.models.Field;
import hedera.starter.cowField.models.FieldCreateDTO;
import hedera.starter.cowField.models.FieldDTO;
import hedera.starter.cowField.models.FieldFullInfoDTO;
import hedera.starter.utilities.EnvUtils;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

@Service
public class FieldService {
    public Client client = Client.forTestnet();
    private String PATH_FIELD_BIN = "D:\\Bernardo\\PolitecnicoLeiria\\MEI_CM\\2ano\\final_project\\COW.API\\src\\main\\java\\hedera\\starter\\cowField\\Field.bin";

    public FieldFullInfoDTO convertToDto(Field field, int currentOccupation, int currentOccupationPercentage) {
        return new FieldFullInfoDTO(field.getIdField(), field.getIdOwner(), field.getIdContract(),
                field.getFieldDescription(), field.getAddress(), field.getLatitude(), field.getLongitude(),
                field.getLimit(), currentOccupation, field.getActive(), field.getObservation(),
                currentOccupationPercentage);
    }

    public Field createField(FieldCreateDTO fieldCreateDTO) {
        Field newField = new Field();
        try {
            File myObj = new File(PATH_FIELD_BIN);
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
                                    .addString(fieldCreateDTO.getLatitude().toString())
                                    .addString(fieldCreateDTO.getLongitude().toString())
                                    .addBool(fieldCreateDTO.getActive())
                                    .addString(fieldCreateDTO.getObservation())
                                    .addString(fieldCreateDTO.getIdOwner()))
                            .execute(client);

                    TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                    System.out.println("Contract Filled " + fileReceipt3.contractId);

                    if (fileReceipt3.contractId != null) {
                        newField = new Field(fileReceipt3.contractId.toString(), fieldCreateDTO.getIdOwner(),
                                fieldCreateDTO.getFieldDescription(), fieldCreateDTO.getAddress(), fieldCreateDTO.getLimit(),
                                fieldCreateDTO.getLatitude(), fieldCreateDTO.getLongitude(), fieldCreateDTO.getActive(),
                                fieldCreateDTO.getObservation() );
                    }
                }
            }
        } catch (FileNotFoundException | TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
        return newField;
    }

    public Field updateField(Field oldField, FieldDTO fieldDTO) {
        try {
            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                    .setContractId(ContractId.fromString(oldField.getIdContract()))
                    .setGas(3000000)
                    .setFunction("setActive", new ContractFunctionParameters().addBool(fieldDTO.getActive()))
                    .execute(client);

            TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
            System.out.println("Status " + fileReceipt.status);

            oldField.setFieldDescription(fieldDTO.getFieldDescription());
            oldField.setAddress(fieldDTO.getAddress());
            oldField.setLimit(fieldDTO.getLimit());
            oldField.setLatitude(fieldDTO.getLatitude());
            oldField.setLongitude(fieldDTO.getLongitude());
            oldField.setActive(fieldDTO.getActive());
            oldField.setObservation(fieldDTO.getObservation());
            return oldField;

        } catch (TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
        return oldField;
    }

    public void deleteField(String idContract){
        Status status = null;
        PrivateKey operatorKey = EnvUtils.getOperatorKey();
        client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

        try {
            assert client.getOperatorAccountId() != null;
            ContractDeleteTransaction transaction = new ContractDeleteTransaction()
                    .setTransferAccountId(client.getOperatorAccountId())
                    .setContractId(ContractId.fromString(idContract));

            TransactionResponse txResponse = transaction.freezeWith(client).sign(operatorKey).execute(client);
            TransactionReceipt receipt = txResponse.getReceipt(client);
            System.out.println("Status "+receipt.status);
        } catch (PrecheckStatusException | TimeoutException | ReceiptStatusException e) {
            e.printStackTrace();
        }
    }
}