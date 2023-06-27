package com.bernardo.figueiredo.cow.api.business.appointment.boundary;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AppointmentMapper {

    public AppointmentDTO mapEntityToDTO(Appointment appointment) {
        long appointmentRequestId = 0;
        if (appointment.getScheduleAppointment() != null) {
            appointmentRequestId = appointment.getScheduleAppointment().getId();
        }
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getIdContract(),
                appointmentRequestId,
                appointment.getBovine().getId(),
                appointment.getVeterinary().getId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentType(),
                appointment.getCost(),
                appointment.getObservation(),
                appointment.getAppointmentStatus());
    }

    public List<AppointmentDTO> mapSourceListToTargetList(List<Appointment> sourceList) {
        List<AppointmentDTO> targetList = new ArrayList<>();
        for (Appointment appointment : sourceList) {
            targetList.add(mapEntityToDTO(appointment));
        }
        return targetList;
    }
}
