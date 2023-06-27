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
import java.math.BigInteger;
import java.util.ArrayList;
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
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_USER_NOT_FOUND);
        }

        return appointments;
    }

    public List<Appointment> createAppointment(AppointmentCreateDTO appointmentCreateDTO) {

        List<Appointment> newAppointments = new ArrayList<>();
        HederaReceipt receipt;
        FileId fileId;

        User user = userService.getUserById(appointmentCreateDTO.getIdVeterinary());
        Bovine bovine = bovineService.getBovineById(appointmentCreateDTO.getIdBovine());

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getHederaContractFile(client);

            validateReceiptStatus(receipt);

            fileId = receipt.getFileId();

            receipt = getHederaContractFileAppend(client, fileId, baseByteCode.getAppointmentByteCode(), 15, 2);

            validateReceiptStatus(receipt);

            Appointment newAppointment = new Appointment();
            newAppointment.setScheduleAppointment(null);
            newAppointment.setBovine(bovine);
            newAppointment.setVeterinary(user);
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

            newAppointment.setIdContract(receipt.getContractId().toString());

            Appointment savedAppointment = appointmentRepository.save(newAppointment);
            newAppointments.add(savedAppointment);

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
                                BigInteger.valueOf(newAppointment.getBovine().getId()))
                        .addUint256(BigInteger.valueOf(
                                newAppointment.getVeterinary().getId()))
                        .addUint256(BigInteger.valueOf(
                                newAppointment.getAppointmentDate().getTime()))
                        .addString(newAppointment.getAppointmentType())
                        .addString(newAppointment.getObservation()));

        return execute(client, contractCreateTransaction);
    }

    public Appointment updateAppointment(long id, AppointmentDTO updateAppointmentDTO) {

        Appointment updateAppointment = appointmentRepository.getAppointmentById(id);
        if (updateAppointment == null) {
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }

        User user = userService.getUserById(updateAppointmentDTO.getIdVeterinary());
        if (user == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        Bovine bovine = bovineService.getBovineById(updateAppointmentDTO.getIdBovine());
        if (bovine == null) {
            throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
        }

        HederaReceipt receipt;

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getAppointmentUpdateReceipt(client, updateAppointmentDTO);

            validateReceiptStatus(receipt);

        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        updateAppointment.setBovine(bovine);
        updateAppointment.setVeterinary(user);
        updateAppointment.setAppointmentDate(updateAppointmentDTO.getAppointmentDate());
        updateAppointment.setAppointmentType(updateAppointmentDTO.getAppointmentType());
        updateAppointment.setCost(updateAppointmentDTO.getCost());
        updateAppointment.setObservation(updateAppointmentDTO.getObservation());
        updateAppointment.setAppointmentStatus(updateAppointmentDTO.getAppointmentStatus());

        return appointmentRepository.save(updateAppointment);
    }

    private HederaReceipt getAppointmentUpdateReceipt(Client client, AppointmentDTO appointmentDTO)
            throws TimeoutException {
        try {
            return buildAppointmentUpdateReceipt(client, appointmentDTO);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_UPDATE_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.APPOINTMENT_UPDATE_FAILED));
        }
    }

    private HederaReceipt buildAppointmentUpdateReceipt(Client client, AppointmentDTO appointmentDTO)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractExecuteTransaction contractCreateTransaction = new ContractExecuteTransaction()
                .setContractId(ContractId.fromString(appointmentDTO.getIdContract()))
                .setGas(300_000)
                .setFunction(
                        "setUpdate",
                        new ContractFunctionParameters()
                                .addUint256(BigInteger.valueOf(appointmentDTO.getIdBovine()))
                                .addUint256(BigInteger.valueOf(appointmentDTO.getIdVeterinary()))
                                .addUint256(BigInteger.valueOf(
                                        appointmentDTO.getAppointmentDate().getTime()))
                                .addString(appointmentDTO.getAppointmentType())
                                .addString(appointmentDTO.getObservation()));

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

    public void deleteAppointment(long id) {

        Appointment deleteAppointment = appointmentRepository.getAppointmentById(id);
        if (deleteAppointment == null) {
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }

        HederaReceipt receipt;

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getAppointmentDeleteReceipt(client, deleteAppointment.getIdContract());

            validateReceiptStatus(receipt);

        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        appointmentRepository.delete(deleteAppointment);
    }

    private HederaReceipt getAppointmentDeleteReceipt(Client client, String appointmentContract)
            throws TimeoutException {
        try {
            return buildAppointmentDeleteReceipt(client, appointmentContract);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_DELETE_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.APPOINTMENT_DELETE_FAILED));
        }
    }

    private HederaReceipt buildAppointmentDeleteReceipt(Client client, String appointmentContractId)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractDeleteTransaction contractDeleteTransaction = new ContractDeleteTransaction()
                .setTransferAccountId(EnvUtils.getOperatorId())
                .setContractId(ContractId.fromString(appointmentContractId));

        return freezeWithSignExecute(client, EnvUtils.getOperatorKey(), contractDeleteTransaction);
    }
}
