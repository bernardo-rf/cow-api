/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.schedule.boundary;

import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseService;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.bernardo.figueiredo.cow.api.business.appointment.boundary.AppointmentService;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentCreateDTO;
import com.bernardo.figueiredo.cow.api.business.bovine.boundary.BovineService;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.schedule.dto.*;
import com.bernardo.figueiredo.cow.api.business.user.boundary.UserService;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleAppointmentService extends BaseService {

    @Autowired
    ScheduleAppointmentRepository scheduleAppointmentRepository;

    @Autowired
    BovineService bovineService;

    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;

    public ScheduleAppointment getScheduleAppointmentById(long id) {

        ScheduleAppointment scheduleAppointment = scheduleAppointmentRepository.getScheduleAppointmentById(id);

        if (scheduleAppointment == null) {
            throw new ErrorCodeException(ErrorCode.SCHEDULE_APPOINTMENT_NOT_FOUND);
        }

        return scheduleAppointment;
    }

    public List<ScheduleAppointment> getScheduleAppointmentByBovineId(long id) {

        List<ScheduleAppointment> scheduleAppointments =
                scheduleAppointmentRepository.getScheduleAppointmentByBovineId(id);

        if (scheduleAppointments.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.SCHEDULE_APPOINTMENT_BOVINE_NOT_FOUND);
        }

        return scheduleAppointments;
    }

    public List<ScheduleAppointment> getScheduleAppointmentByVeterinaryId(long id) {

        List<ScheduleAppointment> scheduleAppointments =
                scheduleAppointmentRepository.getScheduleAppointmentByVeterinaryId(id);

        if (scheduleAppointments.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.SCHEDULE_APPOINTMENT_VETERINARY_NOT_FOUND);
        }

        return scheduleAppointments;
    }

    public List<ScheduleAppointment> getScheduleAppointmentByOwnerId(long id) {

        List<ScheduleAppointment> scheduleAppointments =
                scheduleAppointmentRepository.getScheduleAppointmentByOwnerId(id);

        if (scheduleAppointments.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.SCHEDULE_APPOINTMENT_OWNER_NOT_FOUND);
        }

        return scheduleAppointments;
    }

    public ScheduleAppointment createScheduleAppointment(ScheduleAppointmentCreateDTO scheduleAppointmentCreateDTO) {

        Bovine bovine = bovineService.getBovineById(scheduleAppointmentCreateDTO.getIdBovine());
        User veterinary = userService.getUserById(scheduleAppointmentCreateDTO.getIdVeterinary());
        User owner = userService.getUserById(scheduleAppointmentCreateDTO.getIdOwner());

        if (scheduleAppointmentCreateDTO.getScheduleDate().before(new Date())) {
            throw new ErrorCodeException(ErrorCode.SCHEDULE_APPOINTMENT_DATE_INVALID);
        }

        ScheduleAppointment newScheduleAppointment = new ScheduleAppointment();
        newScheduleAppointment.setScheduleDate(scheduleAppointmentCreateDTO.getScheduleDate());
        newScheduleAppointment.setScheduleStatus(scheduleAppointmentCreateDTO.getScheduleStatus());
        newScheduleAppointment.setMotive(scheduleAppointmentCreateDTO.getMotive());
        newScheduleAppointment.setBovine(bovine);
        newScheduleAppointment.setVeterinary(veterinary);
        newScheduleAppointment.setOwner(owner);
        return scheduleAppointmentRepository.save(newScheduleAppointment);
    }

    public ScheduleAppointment updateScheduleAppointment(long id, ScheduleAppointmentDTO scheduleAppointmentDTO) {

        ScheduleAppointment scheduleAppointment = scheduleAppointmentRepository.getScheduleAppointmentById(id);
        Bovine bovine = bovineService.getBovineById(scheduleAppointmentDTO.getIdBovine());
        User veterinary = userService.getUserById(scheduleAppointmentDTO.getIdVeterinary());
        User owner = userService.getUserById(scheduleAppointmentDTO.getIdOwner());

        if (scheduleAppointmentDTO.getScheduleDate().before(new Date())) {
            throw new ErrorCodeException(ErrorCode.SCHEDULE_APPOINTMENT_DATE_INVALID);
        }

        scheduleAppointment.setScheduleDate(scheduleAppointmentDTO.getScheduleDate());
        scheduleAppointment.setScheduleStatus(scheduleAppointmentDTO.getScheduleStatus());
        scheduleAppointment.setMotive(scheduleAppointmentDTO.getMotive());
        scheduleAppointment.setBovine(bovine);
        scheduleAppointment.setVeterinary(veterinary);
        scheduleAppointment.setOwner(owner);

        return scheduleAppointmentRepository.save(scheduleAppointment);
    }

    public ScheduleAppointment updateScheduleAppointmentStatus(long id, int status) {

        ScheduleAppointment scheduleAppointment = scheduleAppointmentRepository.getScheduleAppointmentById(id);
        if (scheduleAppointment == null) {
            throw new ErrorCodeException(ErrorCode.SCHEDULE_APPOINTMENT_NOT_FOUND);
        }

        scheduleAppointment.setScheduleStatus(status);
        scheduleAppointmentRepository.save(scheduleAppointment);

        if (status == 1) {
            AppointmentCreateDTO appointmentCreateDTO = new AppointmentCreateDTO();
            appointmentCreateDTO.setAppointmentDate(scheduleAppointment.getScheduleDate());
            appointmentCreateDTO.setAppointmentType(scheduleAppointment.getMotive());
            appointmentCreateDTO.setIdAppointmentRequest(scheduleAppointment.getId());
            appointmentCreateDTO.setIdVeterinary(
                    scheduleAppointment.getVeterinary().getId());
            appointmentCreateDTO.setIdBovine(scheduleAppointment.getBovine().getId());
            appointmentService.createAppointment(appointmentCreateDTO);
        }

        return scheduleAppointment;
    }

    public void deleteScheduleAppointment(long id) {

        ScheduleAppointment deleteScheduleAppointment = scheduleAppointmentRepository.getScheduleAppointmentById(id);
        if (deleteScheduleAppointment == null) {
            throw new ErrorCodeException(ErrorCode.SCHEDULE_APPOINTMENT_NOT_FOUND);
        }
        scheduleAppointmentRepository.delete(deleteScheduleAppointment);
    }
}
