/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.field_history;

import cow.starter.bovine.models.Bovine;
import cow.starter.bovine.models.BovineRepository;
import cow.starter.field.models.*;
import cow.starter.field_history.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FieldHistoryService {

    @Autowired
    FieldHistoryRepository fieldHistoryRepository;

    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    BovineRepository bovineRepository;

    public FieldHistoryDTO convertToSimpleDTO(FieldHistory fieldHistory) {
        return new FieldHistoryDTO(fieldHistory.getIdFieldHistory(), fieldHistory.getField().getIdField(),
                fieldHistory.getBovine().getIdBovine(), fieldHistory.getSwitchDate());
    }

    public FieldHistoryFullInfoDTO convertToDTO(FieldHistory fieldHistory, String fieldDescription, String fieldAddress,
                                                long serialNumber) {
        return new FieldHistoryFullInfoDTO(fieldHistory.getIdFieldHistory(), fieldHistory.getField().getIdField(), fieldDescription,
                fieldAddress, fieldHistory.getBovine().getIdBovine(), serialNumber, fieldHistory.getSwitchDate().toString());
    }

    public boolean checkFieldHistoryValues(long idField, long idBovine){
        Field field = fieldRepository.getField(idField);
        if (field != null) {
            Bovine bovine = bovineRepository.getBovine(idBovine);
            return bovine.getIdBovine() != 0;
        }
        return false;
    }

    public List<FieldHistoryFullInfoDTO> getFieldHistoryListFullInfoByIdBovine(long idBovine){
        List<FieldHistoryFullInfoDTO> fieldHistoryFullInfoDTOList = new ArrayList<>();
        List<FieldHistory> fieldHistories = fieldHistoryRepository.getAllFieldsHistoryByIDBovine(idBovine);
        if (!fieldHistories.isEmpty()) {
            Bovine bovine = bovineRepository.getBovine(idBovine);
            if (bovine != null) {
                for (FieldHistory fieldHistory:fieldHistories) {
                    Field field = fieldRepository.getField(fieldHistory.getField().getIdField());
                    if (field != null){
                        fieldHistoryFullInfoDTOList.add(convertToDTO(fieldHistory, field.getFieldDescription(),
                                field.getAddress(), bovine.getSerialNumber()));
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
            if(checkFieldHistoryValues(fieldHistoryCreatedDTO.getIdField(), fieldHistoryCreatedDTO.getIdBovine())) {
                FieldHistory fieldHistory = new FieldHistory(fieldRepository.getField(fieldHistoryCreatedDTO.getIdField()),
                        bovineRepository.getBovine(fieldHistoryCreatedDTO.getIdBovine()), fieldHistoryCreatedDTO.getSwitchDate());
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
            if(checkFieldHistoryValues(fieldHistoryDTO.getIdField(), fieldHistoryDTO.getIdBovine())) {

                FieldHistory fieldHistory = fieldHistoryRepository.getFieldHistory(fieldHistoryDTO.getIdFieldHistory());
                if (fieldHistory != null) {
                    fieldHistory.setField(fieldRepository.getField(fieldHistoryDTO.getIdField()));
                    fieldHistory.setBovine(bovineRepository.getBovine(fieldHistoryDTO.getIdBovine()));
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
