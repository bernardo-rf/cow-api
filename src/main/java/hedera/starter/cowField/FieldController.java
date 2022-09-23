package hedera.starter.cowField;

import hedera.starter.cowBovine.BovineService;
import hedera.starter.cowBovine.models.Bovine;
import hedera.starter.cowBovine.models.BovineDTO;
import hedera.starter.cowField.models.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
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

    @GetMapping("/{fieldId}")
    @ApiOperation("Get field by id")
    public ResponseEntity<FieldFullInfoDTO> getField(@PathVariable long fieldId) throws Exception {
        try {
            Optional<Field> fieldAux = fieldRepository.getFieldByIDField(fieldId);
            if (fieldAux.isPresent()){
                int fieldsCurrentOccupation = fieldRepository.getFieldCurrentOccupation(fieldId);
                int currentOccupationPercentage = (int)(((fieldsCurrentOccupation*1.0)/fieldAux.get().getLimit())*100);
                FieldFullInfoDTO fullInfoDTO = fieldService.convertToDto(fieldAux.get(), fieldsCurrentOccupation, currentOccupationPercentage);
                return ResponseEntity.ok(fullInfoDTO);
            }
        }catch(Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("{idOwner}/full_info")
    @ApiOperation("Get all fields full info")
    public ResponseEntity<List<FieldFullInfoDTO>> getFieldsFullInfo(@PathVariable String idOwner) throws Exception {
        try {
            List<FieldFullInfoDTO> fieldFullInfoDTOList =  new ArrayList<>();
            List<Field> fields = fieldRepository.getAllFieldsByOwner(idOwner);
            if (fields.isEmpty()){
                return ResponseEntity.ok(fieldFullInfoDTOList);
            }
            List<Integer> fieldsCurrentOccupation = fieldRepository.getAllFieldsCurrentOccupation();
            if (!fieldsCurrentOccupation.isEmpty()){
                for (int i=0; i <= fields.size() -1; i++){
                    Field field = fields.get(i);
                    int currentOccupationPercentage = (int)(((fieldsCurrentOccupation.get(i)*1.0)/field.getLimit())*100);
                    FieldFullInfoDTO fieldFullInfoDTO = fieldService.convertToDto(field, fieldsCurrentOccupation.get(i),
                            currentOccupationPercentage);
                    fieldFullInfoDTOList.add(fieldFullInfoDTO);
                }
                return ResponseEntity.ok(fieldFullInfoDTOList);
            }
        }catch (Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/cows/{fieldId}")
    @ApiOperation("Get field cows by id")
    public ResponseEntity<List<BovineDTO>> getFieldBovine(@PathVariable long fieldId) throws Exception {
        try {
            List<BovineDTO> bovineDTOList =  new ArrayList<>();
            List<Bovine> bovineList = fieldRepository.getFieldBovine(fieldId);
            if (!bovineList.isEmpty()){
                for (Bovine bovine: bovineList) {
                    BovineDTO bovineDTO = bovineService.convertToDto(bovine);
                    bovineDTOList.add(bovineDTO);
                }
            }
            return ResponseEntity.ok(bovineDTOList);
        }catch(Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
    }

    @GetMapping("/cows/notIn/{fieldId}")
    @ApiOperation("Get field cows by id")
    public ResponseEntity<List<BovineDTO>> getAllBovineNotIn(@PathVariable long fieldId) throws Exception {
        try {
            List<BovineDTO> bovineDTOList =  new ArrayList<>();
            List<Bovine> bovineList = fieldRepository.getAllBovineNotIn(fieldId);
            if (!bovineList.isEmpty()){
                for (Bovine bovine: bovineList) {
                    BovineDTO bovineDTO = bovineService.convertToDto(bovine);
                    bovineDTOList.add(bovineDTO);
                }
            }
            return ResponseEntity.ok(bovineDTOList);
        }catch(Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
    }

    @PostMapping("/")
    @ApiOperation("Create a field")
    public ResponseEntity<FieldFullInfoDTO> createField(@RequestBody FieldCreateDTO fieldCreateDTO ) throws Exception {
        try {
            if (fieldRepository.findFieldByAddress(fieldCreateDTO.getAddress(), fieldCreateDTO.getIdOwner()) != null) {
                return ResponseEntity.status(403).build();
            }
            Field newField = fieldService.createField(fieldCreateDTO);
            if (newField != null){
                fieldRepository.save(newField);
                FieldFullInfoDTO fullInfoDTO = fieldService.convertToDto(newField, 0, 0);
                if (fullInfoDTO.getIdField() != 0){
                    bovineService.updateBovineLocation(fieldCreateDTO.getBovines(), newField.getIdField());
                    return ResponseEntity.ok(fullInfoDTO);
                }
            }
        }catch (Exception e) {
        throw new Exception("INVALID_OPERATION", e);
    }
        return ResponseEntity.status(401).build();
    }

    @PutMapping("/{fieldID}")
    @ApiOperation("Update a field")
    public ResponseEntity<FieldDTO> updateField( @PathVariable long fieldID, @RequestBody FieldDTO fieldDTO )
            throws Exception {
        try {
            if(fieldID == fieldDTO.getIdField()){
                Optional<Field> oldField = fieldRepository.getFieldByIDField(fieldID);
                if (oldField.isPresent()) {
                    Field updatedField = fieldService.updateField(oldField.get(), fieldDTO);
                    fieldRepository.save(updatedField);

                    FieldFullInfoDTO fullInfoDTO = fieldService.convertToDto(updatedField, 0, 0);
                    if (fullInfoDTO.getIdField() != 0){
                        List<BovineDTO> bovineDTOS = bovineService.updateBovineLocation(fieldDTO.getBovines(), updatedField.getIdField());
                        FieldDTO updatedFieldDTO = new FieldDTO(updatedField.getIdField(), updatedField.getIdOwner(),
                                updatedField.getActive(), updatedField.getFieldDescription(), updatedField.getIdContract(),
                                updatedField.getLatitude(), updatedField.getLongitude(), updatedField.getAddress(),
                                updatedField.getLimit(), updatedField.getObservation(), bovineDTOS);
                        return ResponseEntity.ok(updatedFieldDTO);
                    }
                }
            }
        } catch  (Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }

    @DeleteMapping("/{fieldID}")
    @ApiOperation("Delete a field")
    public ResponseEntity<String> deleteField( @PathVariable long fieldID) throws Exception {
        try {
            Field fieldToDelete = fieldRepository.findById(fieldID).get();
            if (!fieldToDelete.getIdContract().isBlank()){
                fieldService.deleteField(fieldToDelete.getIdContract());
                fieldRepository.deleteById(fieldID);
                return ResponseEntity.ok().build();
            }
        }catch(Exception e){
            throw new Exception("INVALID_OPERATION",e);
        }
        return ResponseEntity.status(401).build();
    }
}
