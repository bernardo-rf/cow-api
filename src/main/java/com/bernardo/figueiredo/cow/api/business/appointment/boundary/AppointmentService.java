/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointment.boundary;

import static com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaExecutor.*;

import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseByteCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseService;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaReceipt;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentCreateDTO;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentDTO;
import com.bernardo.figueiredo.cow.api.business.bovine.boundary.BovineService;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.user.boundary.UserService;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.bernardo.figueiredo.cow.api.utils.EnvUtils;
import com.hedera.hashgraph.sdk.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService extends BaseService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    BaseByteCode baseByteCode;

    @Autowired
    UserService userService;

    @Autowired
    BovineService bovineService;

    public Appointment getAppointmentById(long id) {

        Appointment appointment = appointmentRepository.getAppointmentById(id);

        if (appointment == null) {
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }

        return appointment;
    }

    public List<Appointment> getAppointmentsByBovineId(long id) {

        List<Appointment> appointments = appointmentRepository.getAppointmentsByBovineId(id);

        if (appointments.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_BOVINE_NOT_FOUND);
        }

        return appointments;
    }

    public List<Appointment> getAppointmentsByUserId(long id) {

        List<Appointment> appointments = appointmentRepository.getAppointmentsByUserId(id);

        if (appointments.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_BOVINE_NOT_FOUND);
        }

        return appointments;
    }

    public List<Appointment> getAppointmentsByUserAddress(String address) {

        List<Appointment> appointments = appointmentRepository.getAppointmentsByUserAddress(address);

        if (appointments.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_BOVINE_NOT_FOUND);
        }

        return appointments;
    }

    public List<Appointment> createAppointment(AppointmentCreateDTO appointmentCreateDTO) {

        HederaReceipt receipt;
        FileId fileId;
        List<Appointment> newAppointments = new ArrayList<>();

        User user = userService.getUserById(appointmentCreateDTO.getIdUser());

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getHederaContractFile(client);

            validateReceiptStatus(receipt);

            fileId = receipt.getFileId();

            receipt = getHederaContractFileAppend(client, fileId, baseByteCode.getAppointmentByteCode(), 15, 2);

            validateReceiptStatus(receipt);

            for (long bovineId : appointmentCreateDTO.getBovineIds()) {

                Bovine bovine = bovineService.getBovineById(bovineId);

                Appointment newAppointment = new Appointment();
                newAppointment.setIdContract(receipt.getContractId().toString());
                newAppointment.setAppointmentRequest(null);
                newAppointment.setBovine(bovine);
                newAppointment.setUser(user);
                newAppointment.setAppointmentDate(appointmentCreateDTO.getAppointmentDate());
                newAppointment.setCost(appointmentCreateDTO.getCost());
                newAppointment.setAppointmentType(appointmentCreateDTO.getAppointmentType());
                newAppointment.setAppointmentStatus(appointmentCreateDTO.getStatus());
                newAppointment.setObservation(appointmentCreateDTO.getObservation());

                receipt = getAppointmentDeployReceipt(client, fileId, newAppointment);

                validateReceiptStatus(receipt);

                if (receipt.getContractId() == null) {
                    throw new ErrorCodeException(ErrorCode.HEDERA_CONTRACT_ID_NOT_FOUND);
                }

                Appointment savedAppointment = appointmentRepository.save(newAppointment);
                newAppointments.add(savedAppointment);
            }

        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.APPOINTMENT_DEPLOY_FAILED));
        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        return newAppointments;
    }

    private HederaReceipt getAppointmentDeployReceipt(Client client, FileId fileId, Appointment appointment)
            throws TimeoutException {
        try {
            return buildAppointmentDeployReceipt(client, fileId, appointment);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.APPOINTMENT_DEPLOY_FAILED));
        }
    }

    private HederaReceipt buildAppointmentDeployReceipt(
            Client client, FileId byteCodeFileId, Appointment newAppointment)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractCreateTransaction contractCreateTransaction = new ContractCreateTransaction()
                .setBytecodeFileId(byteCodeFileId)
                .setGas(300_000)
                .setAdminKey(EnvUtils.getOperatorKey())
                .setConstructorParameters(new ContractFunctionParameters()
                        .addUint256(
                                BigInteger.valueOf(newAppointment.getBovine().getIdBovine()))
                        .addUint256(BigInteger.valueOf(newAppointment.getUser().getIdUser()))
                        .addUint256(BigInteger.valueOf(
                                newAppointment.getAppointmentDate().getTime()))
                        .addString(newAppointment.getAppointmentType())
                        .addUint256(BigDecimal.valueOf(newAppointment.getCost()).toBigInteger())
                        .addString(newAppointment.getObservation()));

        return execute(client, contractCreateTransaction);
    }

    public Appointment updateAppointment(long id, AppointmentDTO updateAppointmentDTO) {

        Appointment updateAppointment = appointmentRepository.getAppointmentById(id);
        if (updateAppointment == null) {
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }

        User user = userService.getUserById(updateAppointmentDTO.getIdUser());
        if (user == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        Bovine bovine = bovineService.getBovineById(updateAppointmentDTO.getIdBovine());
        if (bovineService.getBovineById(updateAppointmentDTO.getIdBovine()) == null) {
            throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
        }

        Date appointmentDate;

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            appointmentDate = formatter.parse(updateAppointmentDTO.getAppointmentDate());

        } catch (ParseException e) {
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_UPDATE_FAILED);
        }

        HederaReceipt receipt;

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getAppointmentUpdateReceipt(
                    client, updateAppointment.getIdContract(), updateAppointmentDTO, appointmentDate);

            validateReceiptStatus(receipt);

        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        updateAppointment.setBovine(bovine);
        updateAppointment.setUser(user);
        updateAppointment.setAppointmentDate(appointmentDate);
        updateAppointment.setAppointmentType(updateAppointmentDTO.getAppointmentType());
        updateAppointment.setCost(updateAppointmentDTO.getCost());
        updateAppointment.setObservation(updateAppointmentDTO.getObservation());
        updateAppointment.setAppointmentStatus(updateAppointmentDTO.getStatus());

        return appointmentRepository.save(updateAppointment);
    }

    private HederaReceipt getAppointmentUpdateReceipt(
            Client client, String appointmentContract, AppointmentDTO appointmentDTO, Date appointmentDate)
            throws TimeoutException {
        try {
            return buildAppointmentUpdateReceipt(client, appointmentContract, appointmentDTO, appointmentDate);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.APPOINTMENT_DEPLOY_FAILED));
        }
    }

    private HederaReceipt buildAppointmentUpdateReceipt(
            Client client, String appointmentContractId, AppointmentDTO appointmentDTO, Date appointmentDate)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractExecuteTransaction contractCreateTransaction = new ContractExecuteTransaction()
                .setContractId(ContractId.fromString(appointmentContractId))
                .setGas(300_000)
                .setFunction(
                        "setUpdate",
                        new ContractFunctionParameters()
                                .addUint256(BigInteger.valueOf(appointmentDTO.getIdBovine()))
                                .addUint256(BigInteger.valueOf(appointmentDTO.getIdUser()))
                                .addUint256(BigInteger.valueOf(appointmentDate.getTime())));

        return execute(client, contractCreateTransaction);
    }

    public Appointment updateAppointmentStatus(long id, int status) {

        Appointment appointment = appointmentRepository.getAppointmentById(id);

        if (appointment == null) {
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }

        appointment.setAppointmentStatus(status);
        return appointmentRepository.save(appointment);
    }

    //        public boolean deleteAppointment(long id) {
    //            try {
    //                PrivateKey operatorKey = EnvUtils.getOperatorKey();
    //                client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());
    //
    //                if (client.getOperatorAccountId() != null) {
    //                    Appointment appointmentToDelete = appointmentRepository.getAppointment(idAppointmentRequest);
    //
    //                    ContractDeleteTransaction transaction = new ContractDeleteTransaction()
    //                            .setTransferAccountId(client.getOperatorAccountId())
    //                            .setContractId(ContractId.fromString(appointmentToDelete.getIdContract()));
    //
    //                    TransactionResponse txResponse =
    //                            transaction.freezeWith(client).sign(operatorKey).execute(client);
    //                    TransactionReceipt receipt = txResponse.getReceipt(client);
    //                    Logger.getLogger("STATUS:" + receipt.status);
    //
    //                    if (appointmentToDelete.getIdAppointment() != 0) {
    //                        appointmentRepository.delete(appointmentToDelete);
    //                        return true;
    //                    }
    //                }
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //            }
    //            return false;
    //        }
}
