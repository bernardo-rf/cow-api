/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field_history.boundary;

import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistoryCreatedDTO;
import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistoryDTO;
import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistoryFullInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("Get all fields history records by bovine id")
    public ResponseEntity<List<FieldHistoryFullInfoDTO>> getFieldsFullInfo(@PathVariable long bovineId)
            throws Exception {
        try {
            return ResponseEntity.ok(fieldHistoryService.getFieldHistoryListFullInfoByIdBovine(bovineId));
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PostMapping("/")
    @ApiOperation("Create a field history record")
    public ResponseEntity<FieldHistoryDTO> createFieldHistory(
            @RequestBody FieldHistoryCreatedDTO fieldHistoryCreatedDTO) throws Exception {
        try {
            FieldHistoryDTO fieldHistoryDTO = fieldHistoryService.createFieldHistory(fieldHistoryCreatedDTO);
            if (fieldHistoryDTO.getIdField() != 0) {
                return ResponseEntity.ok(fieldHistoryDTO);
            }
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PutMapping("/{fieldHistoryId}")
    @ApiOperation("Update a field history record")
    public ResponseEntity<FieldHistoryDTO> updateField(
            @PathVariable long fieldHistoryId, @RequestBody FieldHistoryDTO fieldHistoryDTO) throws Exception {
        try {
            if (fieldHistoryId == fieldHistoryDTO.getIdFieldHistory()) {
                FieldHistoryDTO updatedFieldDTO = fieldHistoryService.updateFieldHistory(fieldHistoryDTO);
                if (updatedFieldDTO.getIdField() != 0) {
                    return ResponseEntity.ok(updatedFieldDTO);
                }
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.status(409).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }
}
