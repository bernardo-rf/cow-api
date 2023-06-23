/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field_history.boundary;

import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistory;
import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistoryCreatedDTO;
import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistoryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "historyFields")
@Api("Handles management of fields history data")
@CrossOrigin(maxAge = 3600)
@SuppressWarnings("unused")
public class FieldHistoryController {

    @Autowired
    private FieldHistoryService fieldHistoryService;

    @Autowired
    private FieldHistoryMapper fieldHistoryMapper;

    public FieldHistoryController() {
        fieldHistoryService = new FieldHistoryService();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get all fields history records by id")
    public ResponseEntity<FieldHistoryDTO> getFieldsHistoryByID(@PathVariable long id) {
        FieldHistory fieldHistory = fieldHistoryService.getFieldHistoryById(id);
        return ResponseEntity.ok(fieldHistoryMapper.mapEntityToDTO(fieldHistory));
    }

    @GetMapping("/bovine/{bovineId}")
    @ApiOperation("Get all fields history records by bovine id")
    public ResponseEntity<List<FieldHistoryDTO>> getFieldsHistoryByBovineID(@PathVariable long bovineId) {
        List<FieldHistory> fieldHistories = fieldHistoryService.getFieldHistoryByBovineId(bovineId);
        return ResponseEntity.ok(fieldHistoryMapper.mapSourceListToTargetList(fieldHistories));
    }

    @PostMapping("/")
    @ApiOperation("Create field history record")
    public ResponseEntity<FieldHistoryDTO> createFieldHistory(
            @RequestBody FieldHistoryCreatedDTO fieldHistoryCreatedDTO) {
        FieldHistory fieldHistory = fieldHistoryService.createFieldHistory(fieldHistoryCreatedDTO);
        return ResponseEntity.ok(fieldHistoryMapper.mapEntityToDTO(fieldHistory));
    }
}
