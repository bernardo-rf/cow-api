/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field_history.boundary;

import com.bernardo.figueiredo.cow.api.business.bovine.boundary.BovineRepository;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.field.boundary.FieldRepository;
import com.bernardo.figueiredo.cow.api.business.field.dto.Field;
import com.bernardo.figueiredo.cow.api.business.field_history.dto.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldHistoryService {

    @Autowired
    FieldHistoryRepository fieldHistoryRepository;

    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    BovineRepository bovineRepository;

    public FieldHistoryDTO convertToSimpleDTO(FieldHistory fieldHistory) {
        return new FieldHistoryDTO(
                fieldHistory.getIdFieldHistory(),
                fieldHistory.getField().getIdField(),
                fieldHistory.getBovine().getId(),
                fieldHistory.getSwitchDate());
    }

    public FieldHistoryFullInfoDTO convertToDTO(
            FieldHistory fieldHistory, String fieldDescription, String fieldAddress, long serialNumber) {
        return new FieldHistoryFullInfoDTO(
                fieldHistory.getIdFieldHistory(),
                fieldHistory.getField().getIdField(),
                fieldDescription,
                fieldAddress,
                fieldHistory.getBovine().getId(),
                serialNumber,
                fieldHistory.getSwitchDate().toString());
    }

    public boolean checkFieldHistoryValues(long idField, long idBovine) {
        Field field = fieldRepository.getField(idField);
        if (field != null) {
            Bovine bovine = bovineRepository.getBovineById(idBovine);
            return bovine.getId() != 0;
        }
        return false;
    }

    public List<FieldHistoryFullInfoDTO> getFieldHistoryListFullInfoByIdBovine(long idBovine) {
        List<FieldHistoryFullInfoDTO> fieldHistoryFullInfoDTOList = new ArrayList<>();
        List<FieldHistory> fieldHistories = fieldHistoryRepository.getAllFieldsHistoryByIDBovine(idBovine);
        if (!fieldHistories.isEmpty()) {
            Bovine bovine = bovineRepository.getBovineById(idBovine);
            if (bovine != null) {
                for (FieldHistory fieldHistory : fieldHistories) {
                    Field field =
                            fieldRepository.getField(fieldHistory.getField().getIdField());
                    if (field != null) {
                        fieldHistoryFullInfoDTOList.add(convertToDTO(
                                fieldHistory,
                                field.getFieldDescription(),
                                field.getAddress(),
                                bovine.getSerialNumber()));
                    }
                }
                return fieldHistoryFullInfoDTOList;
            }
        }
        return fieldHistoryFullInfoDTOList;
    }

    public FieldHistoryDTO createFieldHistory(FieldHistoryCreatedDTO fieldHistoryCreatedDTO) {
        FieldHistoryDTO fieldHistoryDTO = new FieldHistoryDTO();
        try {
            if (checkFieldHistoryValues(fieldHistoryCreatedDTO.getIdField(), fieldHistoryCreatedDTO.getIdBovine())) {
                FieldHistory fieldHistory = new FieldHistory(
                        fieldRepository.getField(fieldHistoryCreatedDTO.getIdField()),
                        bovineRepository.getBovineById(fieldHistoryCreatedDTO.getIdBovine()),
                        fieldHistoryCreatedDTO.getSwitchDate());
                fieldHistoryRepository.save(fieldHistory);
                fieldHistoryDTO = convertToSimpleDTO(fieldHistory);
                if (fieldHistoryDTO.getIdFieldHistory() != 0) {
                    return fieldHistoryDTO;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fieldHistoryDTO;
    }

    public FieldHistoryDTO updateFieldHistory(FieldHistoryDTO fieldHistoryDTO) {
        try {
            if (checkFieldHistoryValues(fieldHistoryDTO.getIdField(), fieldHistoryDTO.getIdBovine())) {

                FieldHistory fieldHistory = fieldHistoryRepository.getFieldHistory(fieldHistoryDTO.getIdFieldHistory());
                if (fieldHistory != null) {
                    fieldHistory.setField(fieldRepository.getField(fieldHistoryDTO.getIdField()));
                    fieldHistory.setBovine(bovineRepository.getBovineById(fieldHistoryDTO.getIdBovine()));
                    fieldHistory.setSwitchDate(fieldHistoryDTO.getSwitchDate());
                    fieldHistoryRepository.save(fieldHistory);

                    fieldHistoryDTO = convertToSimpleDTO(fieldHistory);
                    if (fieldHistoryDTO.getIdFieldHistory() != 0) {
                        return fieldHistoryDTO;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new FieldHistoryDTO();
    }
}
