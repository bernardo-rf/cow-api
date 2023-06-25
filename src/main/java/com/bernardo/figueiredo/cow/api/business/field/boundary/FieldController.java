/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field.boundary;

import com.bernardo.figueiredo.cow.api.business.field.dto.Field;
import com.bernardo.figueiredo.cow.api.business.field.dto.FieldCreateDTO;
import com.bernardo.figueiredo.cow.api.business.field.dto.FieldDTO;
import io.swagger.annotations.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "fields")
@Api("Management endpoints to handle fields")
@CrossOrigin(maxAge = 3600)
@SuppressWarnings("unused")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @Autowired
    private FieldMapper fieldMapper;

    public FieldController() {
        fieldService = new FieldService();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get field by id")
    public ResponseEntity<FieldDTO> getFieldById(@PathVariable long id) {
        Field field = fieldService.getFieldById(id);
        return ResponseEntity.ok(fieldMapper.mapEntityToDTO(field));
    }

    @GetMapping("/user/{userWalletId}")
    @ApiOperation("Get fields by user wallet id")
    public ResponseEntity<List<FieldDTO>> getFieldsByUserWalletId(@PathVariable String userWalletId) {
        List<Field> fields = fieldService.getFieldsByUserWalletId(userWalletId);
        return ResponseEntity.ok(fieldMapper.mapSourceListToTargetList(fields));
    }

    @GetMapping("/user/{userWalletId}/available")
    @ApiOperation("Get all fields owned by userWallet not occupied")
    public ResponseEntity<List<FieldDTO>> getFieldsAvailableByUserWalletId(@PathVariable String userWalletId) {
        List<Field> fields = fieldService.getFieldsAvailableByUserWalletId(userWalletId);
        return ResponseEntity.ok(fieldMapper.mapSourceListToTargetList(fields));
    }

    @PostMapping("/")
    @ApiOperation("Create field")
    public ResponseEntity<FieldDTO> createField(@RequestBody FieldCreateDTO fieldCreateDTO) {
        Field field = fieldService.createField(fieldCreateDTO);
        return ResponseEntity.ok(fieldMapper.mapEntityToDTO(field));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update field")
    public ResponseEntity<FieldDTO> updateField(@PathVariable long id, @RequestBody FieldDTO fieldDTO) {
        Field field = fieldService.updateField(id, fieldDTO);
        return ResponseEntity.ok(fieldMapper.mapEntityToDTO(field));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete field")
    public ResponseEntity<String> deleteField(@PathVariable long id) {
        fieldService.deleteField(id);
        return ResponseEntity.ok("Field deleted with success.");
    }
}
