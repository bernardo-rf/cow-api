/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.field_history;

import cow.starter.field.models.*;
import cow.starter.field_history.models.FieldHistoryCreatedDTO;
import cow.starter.field_history.models.FieldHistoryDTO;
import cow.starter.field_history.models.FieldHistoryFullInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
@Api("Handles management of COW Fields History")
@RequestMapping(path = "api/historyFields")
public class FieldHistoryController {

    @Autowired
    private FieldHistoryService fieldHistoryService;

    public FieldHistoryController() {
        fieldHistoryService = new FieldHistoryService();
    }

    @GetMapping("/{bovineId}")
    @ApiOperation("Get all fields by bovineID")
    public ResponseEntity<List<FieldHistoryFullInfoDTO>> getFieldsFullInfo(@PathVariable long bovineId) throws Exception {
        try {
            return ResponseEntity.ok(fieldHistoryService.getFieldHistoryListFullInfoByIdBovine(bovineId));
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @PostMapping("/")
    @ApiOperation("Create a field history")
    public ResponseEntity<FieldHistoryDTO> createFieldHistory(@RequestBody FieldHistoryCreatedDTO fieldHistoryCreatedDTO)
            throws Exception {
        try {
            FieldHistoryDTO fieldHistoryDTO = fieldHistoryService.createFieldHistory(fieldHistoryCreatedDTO);
            if (fieldHistoryDTO.getIdField() != 0){
                return ResponseEntity.ok(fieldHistoryDTO);
            }
            return ResponseEntity.status(404).build();
        }catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PutMapping("/{fieldHistoryID}")
    @ApiOperation("Update a field history")
    public ResponseEntity<FieldHistoryDTO> updateField( @PathVariable long fieldHistoryID,
                                                        @RequestBody FieldHistoryDTO fieldHistoryDTO )
            throws Exception {
        try {
            if(fieldHistoryID == fieldHistoryDTO.getIdFieldHistory()){
                FieldHistoryDTO updatedFieldDTO = fieldHistoryService.updateFieldHistory(fieldHistoryDTO);
                if (updatedFieldDTO.getIdField() != 0){
                    return ResponseEntity.ok(updatedFieldDTO);
                }
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.status(409).build();
        } catch  (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }
}
