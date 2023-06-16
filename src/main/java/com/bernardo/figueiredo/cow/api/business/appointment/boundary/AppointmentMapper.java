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
        if (appointment.getAppointmentRequest() != null) {
            appointmentRequestId = appointment.getAppointmentRequest().getIdAppointmentRequest();
        }
        return new AppointmentDTO(
                appointment.getIdAppointment(),
                appointment.getIdContract(),
                appointmentRequestId,
                appointment.getBovine().getIdBovine(),
                appointment.getUser().getIdUser(),
                appointment.getAppointmentDate().toString(),
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
