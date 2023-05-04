/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointment.boundary;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentCreateDTO;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentDTO;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentFullInfoDTO;
import com.bernardo.figueiredo.cow.api.business.appointmentRequest.boundary.AppointmentRequestRepository;
import com.bernardo.figueiredo.cow.api.business.appointmentRequest.dto.AppointmentRequest;
import com.bernardo.figueiredo.cow.api.business.bovine.boundary.BovineRepository;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.user.boundary.UserRepository;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.bernardo.figueiredo.cow.api.utils.EnvUtils;
import com.hedera.hashgraph.sdk.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AppointmentRequestRepository appointmentRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BovineRepository bovineRepository;

    public Client client = Client.forTestnet();
    private PrivateKey operatorKey = EnvUtils.getOperatorKey();

    public AppointmentDTO convertToDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getIdAppointment(),
                appointment.getIdContract(),
                appointment.getAppointmentRequest().getIdAppointmentRequest(),
                appointment.getBovine().getIdBovine(),
                appointment.getUser().getIdUser(),
                appointment.getAppointmentDate().toString(),
                appointment.getAppointmentType(),
                appointment.getCost(),
                appointment.getObservation(),
                appointment.getAppointmentStatus());
    }

    public AppointmentFullInfoDTO convertFullInfoToDTO(Appointment appointment, long serialNumber) {
        return new AppointmentFullInfoDTO(
                appointment.getIdAppointment(),
                appointment.getIdContract(),
                appointment.getAppointmentRequest().getIdAppointmentRequest(),
                appointment.getBovine().getIdBovine(),
                appointment.getUser().getIdUser(),
                appointment.getAppointmentDate().toString(),
                appointment.getAppointmentType(),
                appointment.getCost(),
                appointment.getObservation(),
                serialNumber,
                appointment.getAppointmentStatus());
    }

    public List<AppointmentFullInfoDTO> getAppointments(List<Appointment> appointmentList) {
        List<AppointmentFullInfoDTO> appointmentDTOList = new ArrayList<>();
        if (!appointmentList.isEmpty()) {
            for (Appointment appointment : appointmentList) {
                Bovine bovine =
                        bovineRepository.getBovine(appointment.getBovine().getIdBovine());
                appointmentDTOList.add(convertFullInfoToDTO(appointment, bovine.getSerialNumber()));
            }
            return appointmentDTOList;
        }
        return appointmentDTOList;
    }

    public boolean checkAppointmentValues(long idUser, long idBovine) {
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
            Appointment newAppointment = new Appointment();
            List<Bovine> bovines = new ArrayList<>();

            for (int i : appointmentCreateDTO.getBovineIds()) {
                Bovine bovine = bovineRepository.getBovine(i);
                bovines.add(bovine);
            }

            for (Bovine bovine : bovines) {
                File myObj = new File(EnvUtils.getProjectPath() + "appointment/Appointment.bin");
                Scanner myReader = new Scanner(myObj);

                if (checkAppointmentValues(appointmentCreateDTO.getIdUser(), bovine.getIdBovine())) {
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
                            Logger.getLogger("Contract Created =" + fileReceipt2);

                            TransactionResponse contractCreateTransaction = new ContractCreateTransaction()
                                    .setBytecodeFileId(bytecodeFileId)
                                    .setGas(300000)
                                    .setAdminKey(operatorKey)
                                    .setConstructorParameters(new ContractFunctionParameters()
                                            .addUint256(BigInteger.valueOf(bovine.getIdBovine()))
                                            .addUint256(BigInteger.valueOf(appointmentCreateDTO.getIdUser()))
                                            .addUint256(BigInteger.valueOf(appointmentCreateDTO
                                                    .getAppointmentDate()
                                                    .getTime()))
                                            .addString(appointmentCreateDTO.getAppointmentType())
                                            .addUint256(BigDecimal.valueOf(appointmentCreateDTO.getCost())
                                                    .toBigInteger())
                                            .addString(appointmentCreateDTO.getObservation()))
                                    .execute(client);

                            TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                            Logger.getLogger("Contract Filled " + fileReceipt3.contractId);

                            ContractId contractId = fileReceipt3.contractId;

                            if (contractId != null) {
                                User user = userRepository.getUser(appointmentCreateDTO.getIdUser());
                                if (user != null) {
                                    AppointmentRequest appointmentRequest =
                                            appointmentRequestRepository.getAppointmentRequest(
                                                    appointmentCreateDTO.getIdAppointmentRequest());
                                    if (appointmentRequest != null) {
                                        newAppointment = new Appointment(
                                                contractId.toString(),
                                                appointmentRequest,
                                                bovine,
                                                user,
                                                appointmentCreateDTO.getAppointmentDate(),
                                                appointmentCreateDTO.getAppointmentType(),
                                                appointmentCreateDTO.getCost(),
                                                appointmentCreateDTO.getObservation(),
                                                appointmentCreateDTO.getStatus());
                                        appointmentRepository.save(newAppointment);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            appointmentDTO = convertToDTO(newAppointment);
            if (appointmentDTO.getIdAppointment() != 0) {
                return appointmentDTO;
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

                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                Date appointmentDate = formatter.parse(appointmentDTO.getAppointmentDate());

                TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                        .setContractId(ContractId.fromString(appointmentDTO.getIdContract()))
                        .setGas(3000000)
                        .setFunction(
                                "setUpdate",
                                new ContractFunctionParameters()
                                        .addUint256(BigInteger.valueOf(appointmentDTO.getIdBovine()))
                                        .addUint256(BigInteger.valueOf(appointmentDTO.getIdUser()))
                                        .addUint256(BigInteger.valueOf(appointmentDate.getTime())))
                        .execute(client);

                TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
                Logger.getLogger("Status " + fileReceipt.status);

                Appointment appointment = appointmentRepository.getAppointment(appointmentDTO.getIdAppointment());
                if (appointment != null) {
                    Bovine bovine = bovineRepository.getBovine(appointmentDTO.getIdBovine());
                    if (bovine != null) {
                        User user = userRepository.getUser(appointmentDTO.getIdUser());
                        if (user != null) {
                            if (appointmentDTO.getIdAppointmentRequest() != 0) {
                                AppointmentRequest appointmentRequest =
                                        appointmentRequestRepository.getAppointmentRequest(
                                                appointmentDTO.getIdAppointmentRequest());
                                appointment.setAppointmentRequest(appointmentRequest);
                            }
                            appointment.setBovine(bovine);
                            appointment.setUser(user);
                            appointment.setAppointmentDate(appointmentDate);
                            appointment.setAppointmentType(appointmentDTO.getAppointmentType());
                            appointment.setCost(appointmentDTO.getCost());
                            appointment.setObservation(appointmentDTO.getObservation());
                            appointment.setAppointmentStatus(appointmentDTO.getStatus());
                            appointmentRepository.save(appointment);
                            return convertToDTO(appointment);
                        }
                    }
                }
            }
        } catch (TimeoutException | PrecheckStatusException | ReceiptStatusException | ParseException e) {
            e.printStackTrace();
        }
        return emptyAppointmentDTO;
    }

    public AppointmentDTO updateAppointmentStatus(long idAppointment, int status) {
        try {
            Appointment appointment = appointmentRepository.getAppointment(idAppointment);
            if (appointment != null) {
                appointment.setAppointmentStatus(status);
                appointmentRepository.save(appointment);

                return convertToDTO(appointment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AppointmentDTO();
    }

    public boolean deleteAppointment(long idAppointmentRequest) {
        try {
            PrivateKey operatorKey = EnvUtils.getOperatorKey();
            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            if (client.getOperatorAccountId() != null) {
                Appointment appointmentToDelete = appointmentRepository.getAppointment(idAppointmentRequest);

                ContractDeleteTransaction transaction = new ContractDeleteTransaction()
                        .setTransferAccountId(client.getOperatorAccountId())
                        .setContractId(ContractId.fromString(appointmentToDelete.getIdContract()));

                TransactionResponse txResponse =
                        transaction.freezeWith(client).sign(operatorKey).execute(client);
                TransactionReceipt receipt = txResponse.getReceipt(client);
                Logger.getLogger("STATUS:" + receipt.status);

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
