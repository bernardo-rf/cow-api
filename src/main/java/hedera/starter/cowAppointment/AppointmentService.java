package hedera.starter.cowAppointment;

import com.hedera.hashgraph.sdk.*;
import hedera.starter.utilities.EnvUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

@Service
public class AppointmentService {

    public Client client = Client.forTestnet();
    private String PATH_APPOINTMENT_BIN = "D:\\Bernardo\\PolitecnicoLeiria\\MEI_CM\\2ano\\final_project\\COW.API\\src\\main\\java\\hedera\\starter\\cowAppointment\\Appointment.bin";
    private PrivateKey operatorKey = EnvUtils.getOperatorKey();

    public ContractId createAppointment(int idBovine, int idVeterinary, Date appointmentDate, String appointmentType,
                                        Double cost, String observation) {
        ContractId newContractId = null;
        try {
            File myObj = new File(PATH_APPOINTMENT_BIN);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
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
                            .setGas(300000)
                            .setAdminKey(operatorKey)
                            .setConstructorParameters(new ContractFunctionParameters().addUint32(idBovine)
                                    .addUint32(idVeterinary).addString(appointmentDate.toString())
                                    .addString(appointmentType).addString(cost.toString()).addString(observation))
                            .execute(client);

                    TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                    System.out.println("Contract Filled " + fileReceipt3.contractId);

                    newContractId = fileReceipt3.contractId;
                }
            }

        } catch (FileNotFoundException | TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }

        return newContractId;
    }

    public void updateAppointment(String idContract, int idBovine, int idVeterinary, Date appointmentDate,
                                  String appointmentType, Double cost, String observation) {
        try {
            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                    .setContractId(ContractId.fromString(idContract))
                    .setGas(300000)
                    .setFunction("updateAppointment", new ContractFunctionParameters().addUint32(idBovine)
                            .addUint32(idVeterinary).addString(appointmentDate.toString()).addString(appointmentType)
                            .addString(cost.toString()).addString(observation))
                    .execute(client);

            TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
            System.out.println("Status " + fileReceipt.status);

        } catch (TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
    }

    public void deleteAppointment(String idContract){
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

        } catch (PrecheckStatusException | TimeoutException | ReceiptStatusException e) {
            e.printStackTrace();
        }
    }

}
