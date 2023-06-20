package com.bernardo.figueiredo.cow.api.business.schedule.boundary;

import com.bernardo.figueiredo.cow.api.business.schedule.dto.ScheduleAppointment;
import com.bernardo.figueiredo.cow.api.business.schedule.dto.ScheduleAppointmentDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScheduleAppointmentMapper {

    public ScheduleAppointmentDTO mapEntityToDTO(ScheduleAppointment scheduleAppointmentDTO) {
        return new ScheduleAppointmentDTO(
                scheduleAppointmentDTO.getId(),
                scheduleAppointmentDTO.getVeterinary().getIdUser(),
                scheduleAppointmentDTO.getOwner().getIdUser(),
                scheduleAppointmentDTO.getBovine().getIdBovine(),
                scheduleAppointmentDTO.getMotive(),
                scheduleAppointmentDTO.getScheduleDate(),
                scheduleAppointmentDTO.getScheduleStatus());
    }

    public List<ScheduleAppointmentDTO> mapSourceListToTargetList(List<ScheduleAppointment> sourceList) {
        List<ScheduleAppointmentDTO> targetList = new ArrayList<>();
        for (ScheduleAppointment schedule : sourceList) {
            targetList.add(mapEntityToDTO(schedule));
        }
        return targetList;
    }
}
