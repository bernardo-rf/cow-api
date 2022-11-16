package cow.starter.Field;

import cow.starter.Bovine.BovineService;
import cow.starter.Bovine.models.Bovine;
import cow.starter.Bovine.models.BovineDTO;
import cow.starter.Bovine.models.BovineFullInfoDTO;
import cow.starter.Field.models.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
@Api("Handles management of COW Fields")
@RequestMapping(path = "api/fields")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @Autowired
    private BovineService bovineService;

    @Autowired
    private FieldRepository fieldRepository;

    public FieldController() {
        fieldService = new FieldService();
        bovineService = new BovineService();
    }

    @GetMapping("{ownerId}/full_info")
    @ApiOperation("Get all fields full info")
    public ResponseEntity<List<FieldFullInfoDTO>> getFieldsByIDOwner(@PathVariable String ownerId) throws Exception {
        try {
            return ResponseEntity.ok(fieldService.getFieldsByIDOwner(ownerId));
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("{ownerId}/not_occupied")
    @ApiOperation("Get all fields full info")
    public ResponseEntity<List<FieldFullInfoDTO>> getFieldsNotOccupied(@PathVariable String ownerId) throws Exception {
        try {
            return ResponseEntity.ok(fieldService.getFieldsNotOccupied(ownerId));
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{fieldId}")
    @ApiOperation("Get field by id")
    public ResponseEntity<FieldFullInfoDTO> getField(@PathVariable long fieldId) throws Exception {
        try {
            FieldFullInfoDTO fullInfoDTO = fieldService.getField(fieldId);
            if (fullInfoDTO.getIdField() != 0) {
                return ResponseEntity.ok(fullInfoDTO);
            }
            return ResponseEntity.status(404).build();
        }catch(Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/bovines/{fieldId}")
    @ApiOperation("Get field cows by id")
    public ResponseEntity<FieldDTO> getFieldBovine(@PathVariable long fieldId) throws Exception {
        try {
            return ResponseEntity.ok(fieldService.getFieldBovines(fieldId));
        }catch(Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/bovines/{fieldId}/notIn")
    @ApiOperation("Get field cows by id")
    public ResponseEntity<List<BovineFullInfoDTO>> getAllBovineNotIn(@PathVariable long fieldId) throws Exception {
        try {
            return ResponseEntity.ok(fieldService.getFieldBovinesNotIn(fieldId));
        }catch(Exception e){
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
            } else if (fieldDTO.getIdField() != 0){
                return ResponseEntity.ok(fieldDTO);
            }
            return ResponseEntity.status(404).build();
        }catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PutMapping("/{fieldID}")
    @ApiOperation("Update a field")
    public ResponseEntity<FieldDTO> updateField( @PathVariable long fieldID, @RequestBody FieldDTO fieldDTO )
            throws Exception {
        try {
            if(fieldID == fieldDTO.getIdField()){
                FieldDTO updatedFieldDTO = fieldService.updateField(fieldDTO);
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

    @DeleteMapping("/{fieldID}")
    @ApiOperation("Delete a field")
    public ResponseEntity<String> deleteField( @PathVariable long fieldID) throws Exception {
        try {
            if (fieldService.deleteField(fieldID)){
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(404).build();
        }catch(Exception e){
            throw new Exception("ERROR: ",e);
        }
    }
}
