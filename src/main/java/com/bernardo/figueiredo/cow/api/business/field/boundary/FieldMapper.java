package com.bernardo.figueiredo.cow.api.business.field.boundary;

import com.bernardo.figueiredo.cow.api.business.field.dto.Field;
import com.bernardo.figueiredo.cow.api.business.field.dto.FieldDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FieldMapper {

    public FieldDTO mapEntityToDTO(Field field) {
        return new FieldDTO(
                field.getId(),
                field.getOwner().getIdUser(),
                field.getIdContract(),
                field.getFieldDescription(),
                field.getLatitude(),
                field.getLongitude(),
                field.getAddress(),
                field.getMaxCapacityLimit(),
                field.getActive(),
                field.getObservation());
    }

    public List<FieldDTO> mapSourceListToTargetList(List<Field> sourceList) {
        List<FieldDTO> targetList = new ArrayList<>();
        for (Field field : sourceList) {
            targetList.add(mapEntityToDTO(field));
        }
        return targetList;
    }
}
