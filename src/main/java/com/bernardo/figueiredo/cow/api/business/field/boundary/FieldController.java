/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field.boundary;

import com.bernardo.figueiredo.cow.api.business.bovine.dto.BovineFullInfoDTO;
import com.bernardo.figueiredo.cow.api.business.field.dto.FieldCreateDTO;
import com.bernardo.figueiredo.cow.api.business.field.dto.FieldDTO;
import com.bernardo.figueiredo.cow.api.business.field.dto.FieldFullInfoDTO;
import io.swagger.annotations.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 3600)
@Api("Handles management of COW Fields")
@RequestMapping(path = "api/fields")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    public FieldController() {
        fieldService = new FieldService();
    }

    @GetMapping("{userWallet}/full-info")
    @ApiOperation("Get all fields full info")
    public ResponseEntity<List<FieldFullInfoDTO>> getFieldsByIDOwner(@PathVariable String userWallet) throws Exception {
        try {
            return ResponseEntity.ok(fieldService.getFieldsByIDOwner(userWallet));
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("{userWallet}/not-occupied")
    @ApiOperation("Get all fields owned by userWallet not occupied")
    public ResponseEntity<List<FieldFullInfoDTO>> getFieldsNotOccupied(@PathVariable String userWallet)
            throws Exception {
        try {
            return ResponseEntity.ok(fieldService.getFieldsNotOccupied(userWallet));
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{fieldId}")
    @ApiOperation("Get field by filed id")
    public ResponseEntity<FieldFullInfoDTO> getField(@PathVariable long fieldId) throws Exception {
        try {
            FieldFullInfoDTO fullInfoDTO = fieldService.getField(fieldId);
            if (fullInfoDTO.getIdField() != 0) {
                return ResponseEntity.ok(fullInfoDTO);
            }
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{fieldId}/bovines")
    @ApiOperation("Get field bovines by field id")
    public ResponseEntity<FieldDTO> getFieldBovine(@PathVariable long fieldId) throws Exception {
        try {
            return ResponseEntity.ok(fieldService.getFieldBovines(fieldId));
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{fieldId}/bovines/not-in")
    @ApiOperation("Get field bovines not in by field id")
    public ResponseEntity<List<BovineFullInfoDTO>> getAllBovineNotIn(@PathVariable long fieldId) throws Exception {
        try {
            return ResponseEntity.ok(fieldService.getFieldBovinesNotIn(fieldId));
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PostMapping("/")
    @ApiOperation("Create a field")
    public ResponseEntity<FieldDTO> createField(@RequestBody FieldCreateDTO fieldCreateDTO) throws Exception {
        try {
            FieldDTO fieldDTO = fieldService.createField(fieldCreateDTO);
            if (fieldDTO.getIdField() == 999999) {
                return ResponseEntity.status(409).build();
            } else if (fieldDTO.getIdField() != 0) {
                return ResponseEntity.ok(fieldDTO);
            }
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PutMapping("/{fieldID}")
    @ApiOperation("Update a field")
    public ResponseEntity<FieldDTO> updateField(@PathVariable long fieldID, @RequestBody FieldDTO fieldDTO)
            throws Exception {
        try {
            if (fieldID == fieldDTO.getIdField()) {
                FieldDTO updatedFieldDTO = fieldService.updateField(fieldDTO);
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

    @DeleteMapping("/{fieldID}")
    @ApiOperation("Delete a field")
    public ResponseEntity<String> deleteField(@PathVariable long fieldID) throws Exception {
        try {
            if (fieldService.deleteField(fieldID)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }
}
