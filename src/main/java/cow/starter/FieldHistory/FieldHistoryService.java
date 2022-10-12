package cow.starter.FieldHistory;

import cow.starter.Bovine.models.Bovine;
import cow.starter.Bovine.models.BovineRepository;
import cow.starter.Field.models.*;
import cow.starter.FieldHistory.models.*;
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
        return new FieldHistoryDTO(fieldHistory.getIdFieldHistory(), fieldHistory.getIdField(),
                fieldHistory.getIdBovine(), fieldHistory.getSwitchDate());
    }

    public FieldHistoryFullInfoDTO convertToDTO(FieldHistory fieldHistory, String fieldDescription, String fieldAddress,
                                                long serialNumber) {
        return new FieldHistoryFullInfoDTO(fieldHistory.getIdFieldHistory(), fieldHistory.getIdField(), fieldDescription,
                fieldAddress, fieldHistory.getIdBovine(), serialNumber, fieldHistory.getSwitchDate());
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
                    Field field = fieldRepository.getField(fieldHistory.getIdField());
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
                FieldHistory fieldHistory = new FieldHistory(fieldHistoryCreatedDTO.getIdField(),
                        fieldHistoryCreatedDTO.getIdBovine(), fieldHistoryCreatedDTO.getSwitchDate());
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
                    fieldHistory.setIdField(fieldHistoryDTO.getIdField());
                    fieldHistory.setIdBovine(fieldHistoryDTO.getIdBovine());
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
