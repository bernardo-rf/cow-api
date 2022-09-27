package cow.starter.Appointment;

import com.hedera.hashgraph.sdk.*;
import cow.starter.Appointment.models.Appointment;
import cow.starter.Appointment.models.AppointmentCreateDTO;
import cow.starter.Appointment.models.AppointmentDTO;
import cow.starter.Appointment.models.AppointmentRepository;
import cow.starter.Bovine.models.Bovine;
import cow.starter.Bovine.models.BovineRepository;
import cow.starter.User.models.User;
import cow.starter.User.models.UserRepository;
import cow.starter.utilities.EnvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BovineRepository bovineRepository;

    public Client client = Client.forTestnet();
    private PrivateKey operatorKey = EnvUtils.getOperatorKey();

    public AppointmentDTO convertToDTO(Appointment appointment) {
        return new AppointmentDTO(appointment.getIdAppointment(), appointment.getIdContract(),
                appointment.getIdBovine(), appointment.getIdUser(), appointment.getAppointmentDate(),
                appointment.getAppointmentType(), appointment.getCost(), appointment.getObservation());
    }

    public List<AppointmentDTO> getAppointments(List<Appointment> appointmentList){
        List<AppointmentDTO> appointmentDTOList = new ArrayList<>();
        if (!appointmentList.isEmpty()) {
            for (Appointment appointment : appointmentList) {
                appointmentDTOList.add(convertToDTO(appointment));
            }
            return appointmentDTOList;
        }
        return appointmentDTOList;
    }

    public boolean checkAppointmentValues(long idUser, long idBovine){
        User user = userRepository.getUser(idUser);
        if (user != null) {
            Bovine bovine = bovineRepository.getBovine(idBovine);
            if (bovine != null) {
                return bovine.getIdBovine() != 0;
            }
        }
        return false;
    }

    public AppointmentDTO createAppointment(AppointmentCreateDTO appointmentCreateDTO) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        try {
            File myObj = new File("D:\\Bernardo\\PolitecnicoLeiria\\MEI_CM\\2ano\\final_project\\COW.API\\src\\main\\java\\cow\\starter\\Appointment\\Appointment.bin");
            Scanner myReader = new Scanner(myObj);

            if (checkAppointmentValues(appointmentCreateDTO.getIdUser(), appointmentCreateDTO.getIdBovine())) {
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
                                .setConstructorParameters(new ContractFunctionParameters()
                                        .addUint256(BigInteger.valueOf(appointmentCreateDTO.getIdBovine()))
                                        .addUint256(BigInteger.valueOf(appointmentCreateDTO.getIdUser()))
                                        .addUint256(BigInteger.valueOf(appointmentCreateDTO.getAppointmentDate().getTime()))
                                        .addString(appointmentCreateDTO.getAppointmentType())
                                        .addUint256(BigDecimal.valueOf(appointmentCreateDTO.getCost()).toBigInteger())
                                        .addString(appointmentCreateDTO.getObservation()))
                                .execute(client);

                        TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                        System.out.println("Contract Filled " + fileReceipt3.contractId);

                        ContractId contractId = fileReceipt3.contractId;

                        if (contractId != null){
                            Appointment newAppointment = new Appointment(contractId.toString(),
                                    appointmentCreateDTO.getIdBovine(), appointmentCreateDTO.getIdUser(),
                                    appointmentCreateDTO.getAppointmentDate(), appointmentCreateDTO.getAppointmentType(),
                                    appointmentCreateDTO.getCost(), appointmentCreateDTO.getObservation());
                            appointmentRepository.save(newAppointment);

                            appointmentDTO = convertToDTO(newAppointment);
                            if (appointmentDTO.getIdAppointment() != 0) {
                                return appointmentDTO;
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException | TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
        return appointmentDTO;
    }


    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO) {
        AppointmentDTO emptyAppointmentDTO = new AppointmentDTO();
        try {
            if (checkAppointmentValues(appointmentDTO.getIdUser(), appointmentDTO.getIdBovine())) {
                client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

                TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                        .setContractId(ContractId.fromString(appointmentDTO.getIdContract()))
                        .setGas(3000000)
                        .setFunction("setUpdate", new ContractFunctionParameters()
                                .addUint256(BigInteger.valueOf(appointmentDTO.getIdBovine()))
                                .addUint256(BigInteger.valueOf(appointmentDTO.getIdUser()))
                                .addUint256(BigInteger.valueOf(appointmentDTO.getAppointmentDate().getTime())))
                        .execute(client);

                TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
                System.out.println("Status " + fileReceipt.status);

                Appointment appointment = appointmentRepository.getAppointment(appointmentDTO.getIdAppointment());
                if (appointment != null) {
                    appointment.setIdBovine(appointmentDTO.getIdBovine());
                    appointment.setIdUser(appointmentDTO.getIdUser());
                    appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
                    appointment.setAppointmentType(appointmentDTO.getAppointmentType());
                    appointment.setCost(appointmentDTO.getCost());
                    appointment.setObservation(appointmentDTO.getObservation());
                    appointmentRepository.save(appointment);
                    return convertToDTO(appointment);
                }
            }
        } catch (TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
        return emptyAppointmentDTO;
    }

    public boolean deleteAppointment(long idAppointmentRequest){
        try {
            PrivateKey operatorKey = EnvUtils.getOperatorKey();
            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            if (client.getOperatorAccountId() != null){
                Appointment appointmentToDelete = appointmentRepository.getAppointment(idAppointmentRequest);

                ContractDeleteTransaction transaction = new ContractDeleteTransaction()
                        .setTransferAccountId(client.getOperatorAccountId())
                        .setContractId(ContractId.fromString(appointmentToDelete.getIdContract()));

                TransactionResponse txResponse = transaction.freezeWith(client).sign(operatorKey).execute(client);
                TransactionReceipt receipt = txResponse.getReceipt(client);
                System.out.println("STATUS:" + receipt.status);

                if (appointmentToDelete.getIdAppointment() != 0) {
                    appointmentRepository.delete(appointmentToDelete);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
