package hedera.starter.cowField;

import com.hedera.hashgraph.sdk.ContractId;
import hedera.starter.cowBovine.BovineService;
import hedera.starter.cowBovine.models.Bovine;
import hedera.starter.cowBovine.models.BovineDTO;
import hedera.starter.cowField.models.Field;
import hedera.starter.cowField.models.FieldDTO;
import hedera.starter.cowField.models.FieldFullInfoDTO;
import hedera.starter.cowField.models.FieldRepository;
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

    @GetMapping("/")
    @ApiOperation("Get all fields")
    public ResponseEntity<List<FieldDTO>> getAllFields() throws Exception {
        try {
            List<FieldDTO> fieldFullInfoDTOList =  new ArrayList<>();
            List<Field> fields = fieldRepository.getAllFields();
            if (fields.isEmpty()){
                return ResponseEntity.ok(fieldFullInfoDTOList);
            }
            for (Field field:fields) {
                FieldDTO fieldDTO = new FieldDTO(field.getIdField(), field.getActive(),
                        field.getFieldDescription(), field.getIdContract(), field.getLatitude(), field.getLongitude(),
                        field.getAddress(), field.getLimit(), field.getObservation());
                fieldFullInfoDTOList.add(fieldDTO);
            }
            return ResponseEntity.ok(fieldFullInfoDTOList);
        }catch (Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
    }

    @GetMapping("/{fieldId}")
    @ApiOperation("Get field by id")
    public ResponseEntity<FieldFullInfoDTO> getField(@PathVariable long fieldId) throws Exception {
        try {
            Optional<Field> fieldAux = fieldRepository.getFieldByIDField(fieldId);
            if (fieldAux.isPresent()){
                List<Integer> fieldsCurrentOccupation = fieldRepository.getAllFieldsCurrentOccupation();
                if (!fieldsCurrentOccupation.isEmpty()){
                    Field field = fieldAux.get();
                    int currentOccupationPercentage = fieldsCurrentOccupation.get(0);
                    FieldFullInfoDTO fieldFullInfoDTO = fieldService.convertToDto(field, currentOccupationPercentage);
                    return ResponseEntity.ok(fieldFullInfoDTO);
                }
            }
        }catch(Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/full_info")
    @ApiOperation("Get all fields full info")
    public ResponseEntity<List<FieldFullInfoDTO>> getFieldsFullInfo() throws Exception {
        try {
            List<FieldFullInfoDTO> fieldFullInfoDTOList =  new ArrayList<>();
            List<Field> fields = fieldRepository.getAllFields();
            if (fields.isEmpty()){
                return ResponseEntity.ok(fieldFullInfoDTOList);
            }
            List<Integer> fieldsCurrentOccupation = fieldRepository.getAllFieldsCurrentOccupation();
            if (!fieldsCurrentOccupation.isEmpty()){
                for (int i=0; i <= fields.size() -1; i++){
                    Field field = fields.get(i);
                    int currentOccupationPercentage = (int)(((fieldsCurrentOccupation.get(i)*1.0)/field.getLimit())*100);
                    FieldFullInfoDTO fieldFullInfoDTO = fieldService.convertToDto(field, currentOccupationPercentage);
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
                return ResponseEntity.ok(bovineDTOList);
            }
        }catch(Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }



    @PostMapping("/")
    @ApiOperation("Create a field")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "fieldDescription",type = "string", example = "West Field ",
                    value = "define the description of the field."),
            @ApiImplicitParam( name = "address",type = "string",
                    example = "537-3270 1109 19th St. Gil, Nebraska(NE)",
                    value = "define the description of the field."),
            @ApiImplicitParam( name = "limit",type = "int", example = "100",
                    value = "define the limit occupation of the field."),
            @ApiImplicitParam( name = "latitude", type = "double", example = "37.89143",
                    value = "define the latitude of the field."),
            @ApiImplicitParam( name = "longitude", type = "double", example = "-55.86727",
                    value = "define the longitude of the field."),
            @ApiImplicitParam( name = "active", type = "boolean", example = "1",
                    value = "define the active of the field."),
            @ApiImplicitParam( name = "observation", type = "string", example = "Observation about the field",
                    value = "define the observation of the field.")
    })
    public Field createField( @RequestParam(defaultValue = "Field 1") String fieldDescription,
                              @RequestParam(defaultValue = "537-3270 1109 19th St. Gil, Nebraska(NE)")
                                      String address,
                              @RequestParam(defaultValue = "100") int limit,
                              @RequestParam(defaultValue = "10.10101") Double latitude,
                              @RequestParam(defaultValue = "-55.86727") Double longitude,
                              @RequestParam(defaultValue = "1") Boolean active,
                              @RequestParam(defaultValue = "") String observation) {
                ContractId idContract = fieldService.createField(fieldDescription, latitude, longitude, active,
                        observation);
                Field newField = new Field(idContract.toString(), fieldDescription, address, limit, latitude, longitude,
                        active, observation );
        return fieldRepository.save(newField);
    }

    @PutMapping("/{idField}")
    @ApiOperation("Update a field")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "fieldDescription",type = "string", example = "West Field ",
                    value = "define the description of the field."),
            @ApiImplicitParam( name = "address",type = "string",
                    example = "537-3270 1109 19th St. Gil, Nebraska(NE)",
                    value = "define the description of the field."),
            @ApiImplicitParam( name = "limit",type = "int", example = "100",
                    value = "define the limit occupation of the field."),
            @ApiImplicitParam( name = "latitude", type = "double", example = "37.89143",
                    value = "define the latitude of the field."),
            @ApiImplicitParam( name = "longitude", type = "double", example = "-55.86727",
                    value = "define the longitude of the field."),
            @ApiImplicitParam( name = "active", type = "boolean", example = "1",
                    value = "define the active of the field."),
            @ApiImplicitParam( name = "observation", type = "string", example = "Observation about the field",
                    value = "define the observation of the field.")
    })
    public Field updateField( @PathVariable long idField,
                              @RequestParam(defaultValue = "Field 1") String fieldDescription,
                              @RequestParam(defaultValue = "537-3270 1109 19th St. Gil, Nebraska(NE)")
                                          String address,
                              @RequestParam(defaultValue = "100") int limit,
                              @RequestParam(defaultValue = "37.89143") Double latitude,
                              @RequestParam(defaultValue = "10.10101") Double longitude,
                              @RequestParam(defaultValue = "1") Boolean active,
                              @RequestParam(defaultValue = "") String observation) {

        Field oldField = fieldRepository.findById(idField).get();

        if (oldField != null){
            fieldService.updateField(oldField.getIdContract(), fieldDescription, latitude, longitude, active, observation);
            oldField.setFieldDescription(fieldDescription);
            oldField.setAddress(address);
            oldField.setLimit(limit);
            oldField.setLatitude(latitude);
            oldField.setLongitude(longitude);
            oldField.setActive(active);
            oldField.setObservation(observation);
            return fieldRepository.save(oldField);
        }else{
            return new Field();
        }
    }

    @DeleteMapping("/{idField}")
    @ApiOperation("Delete a field")
    public void appointmentField( @PathVariable long idField) {
        Field fieldToDelete = fieldRepository.findById(idField).get();
        fieldService.deleteField(fieldToDelete.getIdContract());
        fieldRepository.deleteById(idField);
    }
}
