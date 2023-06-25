package com.bernardo.figueiredo.cow.api.business.field_history.boundary;

import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistory;
import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistoryDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FieldHistoryMapper {
    public FieldHistoryDTO mapEntityToDTO(FieldHistory fieldHistory) {
        return new FieldHistoryDTO(
                fieldHistory.getId(),
                fieldHistory.getField().getId(),
                fieldHistory.getBovine().getId(),
                fieldHistory.getSwitchDate());
    }

    public List<FieldHistoryDTO> mapSourceListToTargetList(List<FieldHistory> sourceList) {
        List<FieldHistoryDTO> targetList = new ArrayList<>();
        for (FieldHistory fieldHistory : sourceList) {
            targetList.add(mapEntityToDTO(fieldHistory));
        }
        return targetList;
    }
}
