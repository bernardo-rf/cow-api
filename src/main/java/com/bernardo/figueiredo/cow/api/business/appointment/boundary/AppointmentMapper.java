package com.bernardo.figueiredo.cow.api.business.appointment.boundary;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentDTO;
import java.lang.reflect.Type;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

@Service
public class AppointmentMapper {
    private final ModelMapper modelMapper;

    public AppointmentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AppointmentDTO mapEntityToDTO(Appointment appointment) {
        return modelMapper.map(appointment, AppointmentDTO.class);
    }

    public List<AppointmentDTO> mapSourceListToTargetList(List<Appointment> sourceList) {
        Type targetListType = new TypeToken<List<AppointmentDTO>>() {}.getType();
        return modelMapper.map(sourceList, targetListType);
    }
}
