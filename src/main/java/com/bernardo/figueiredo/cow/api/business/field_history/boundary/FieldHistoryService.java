/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field_history.boundary;

import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.bernardo.figueiredo.cow.api.business.bovine.boundary.BovineService;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.field.boundary.FieldService;
import com.bernardo.figueiredo.cow.api.business.field.dto.Field;
import com.bernardo.figueiredo.cow.api.business.field_history.dto.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldHistoryService {

    @Autowired
    FieldHistoryRepository fieldHistoryRepository;

    @Autowired
    FieldService fieldService;

    @Autowired
    BovineService bovineService;

    public FieldHistory getFieldHistoryById(long id) {
        FieldHistory fieldHistory = fieldHistoryRepository.getFieldHistoryById(id);

        if (fieldHistory == null) {
            throw new ErrorCodeException(ErrorCode.FIELD_HISTORY_NOT_FOUND);
        }

        return fieldHistory;
    }

    public List<FieldHistory> getFieldHistoryByBovineId(long idBovine) {
        List<FieldHistory> fieldHistories = fieldHistoryRepository.getFieldHistoryByBovineId(idBovine);

        if (fieldHistories.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.FIELD_HISTORY_NOT_FOUND);
        }

        return fieldHistories;
    }

    public FieldHistory createFieldHistory(FieldHistoryCreatedDTO fieldHistoryCreatedDTO) {

        Field field = fieldService.getFieldById(fieldHistoryCreatedDTO.getIdField());
        Bovine bovine = bovineService.getBovineById(fieldHistoryCreatedDTO.getIdBovine());

        FieldHistory newFieldHistory = new FieldHistory();
        newFieldHistory.setField(field);
        newFieldHistory.setBovine(bovine);
        newFieldHistory.setSwitchDate(fieldHistoryCreatedDTO.getSwitchDate());

        return fieldHistoryRepository.save(newFieldHistory);
    }
}
