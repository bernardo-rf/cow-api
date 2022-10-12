package cow.starter.Bovine;

import cow.starter.Bovine.models.*;
import cow.starter.Field.models.Field;
import cow.starter.Field.models.FieldRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@Api("Handles management of COW Bovine")
@RequestMapping(path = "api/bovines")
public class BovineController {
    @Autowired
    BovineService bovineService;

    @Autowired
    BovineRepository bovineRepository;

    @Autowired
    FieldRepository fieldRepository;

    public BovineController() { bovineService = new BovineService(); }

    @GetMapping("/")
    @ApiOperation("Get all bovines")
    public ResponseEntity<List<BovineDTO>> getAllBovines() throws Exception {
        try {
            List<BovineDTO> bovineDTOList =  new ArrayList<>();
            List<Bovine> bovines = bovineRepository.getAllBovine();
            if (bovines.isEmpty()){
                return ResponseEntity.ok(bovineDTOList);
            }
            for (Bovine bovine:bovines) {
                BovineDTO bovineDTO = bovineService.convertToDTO(bovine);
                bovineDTOList.add(bovineDTO);
            }
            return ResponseEntity.ok(bovineDTOList);
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{ownerId}/male")
    @ApiOperation("Get all bovines owned male")
    public ResponseEntity<List<BovineDTO>> getAllBovinesByIDOwnerMale(@PathVariable String ownerId,
                                                                      @RequestParam long idBovine) throws Exception {
        try {
            List<BovineDTO> bovineDTOList =  new ArrayList<>();
            List<Bovine> bovines = bovineRepository.getAllBovineMaleIdOwner(ownerId, idBovine);
            if (bovines.isEmpty()){
                return ResponseEntity.ok(bovineDTOList);
            }
            for (Bovine bovine:bovines) {
                BovineDTO bovineDTO = bovineService.convertToDTO(bovine);
                bovineDTOList.add(bovineDTO);
            }
            return ResponseEntity.ok(bovineDTOList);
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{ownerId}/feminine")
    @ApiOperation("Get all bovines owned male")
    public ResponseEntity<List<BovineDTO>> getAllBovinesByIDOwnerFeminine(@PathVariable String ownerId,
                                                                          @RequestParam long idBovine) throws Exception {
        try {
            List<BovineDTO> bovineDTOList =  new ArrayList<>();
            List<Bovine> bovines = bovineRepository.getAllBovineFeminineIdOwner(ownerId, idBovine);
            if (bovines.isEmpty()){
                return ResponseEntity.ok(bovineDTOList);
            }
            for (Bovine bovine:bovines) {
                BovineDTO bovineDTO = bovineService.convertToDTO(bovine);
                bovineDTOList.add(bovineDTO);
            }
            return ResponseEntity.ok(bovineDTOList);
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/genealogy/{bovineId}")
    @ApiOperation("Get all genealogy by idBovine")
    public ResponseEntity<List<BovineDTO>> getGenealogy(@PathVariable long bovineId) throws Exception {
        try {
            List<BovineDTO> bovineDTOList =  new ArrayList<>();

            List<Bovine> bovines = bovineRepository.getGenealogy(bovineId);
            if (bovines.isEmpty()){
                return ResponseEntity.ok(bovineDTOList);
            }
            for (Bovine bovine:bovines) {
                BovineDTO bovineDTO = bovineService.convertToDTO(bovine);
                bovineDTOList.add(bovineDTO);
            }
            return ResponseEntity.ok(bovineDTOList);
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{bovineId}")
    @ApiOperation("Get bovine by idBovine")
    public ResponseEntity<BovineFullInfoDTO> getBovine(@PathVariable long bovineId) throws Exception {
            BovineFullInfoDTO fullInfoDTO = new BovineFullInfoDTO();
        try {
            Bovine bovine = bovineRepository.getBovine(bovineId);
            if (bovine != null){
                Field field = fieldRepository.getField(bovine.getIdField());
                    if (bovine.getIdField() == field.getIdField()) {
                         fullInfoDTO = new BovineFullInfoDTO(bovine.getIdBovine(),
                                bovine.getIdContract(), bovine.getIdOwner(), bovine.getIdField(),
                                bovine.getSerialNumber(), bovine.getBirthDate(), bovine.getWeight(),
                                bovine.getHeight(), bovine.getBreed(), bovine.getColor(), bovine.getActive(),
                                bovine.getObservation(), bovine.getIdBovineParent1(), bovine.getIdBovineParent2(),
                                bovine.getGender(), field.getAddress(), bovine.getImageCID());
                    }
            }
            return ResponseEntity.ok(fullInfoDTO);
        }catch(Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{ownerId}/own")
    @ApiOperation("Get bovines by idOwner")
    public ResponseEntity<List<BovineFullInfoDTO>> getOwnedBovines(@PathVariable String ownerId) throws Exception {
        try {
            List<BovineFullInfoDTO> bovineDTOList =  new ArrayList<>();
            List<Bovine> bovines = bovineRepository.getAllBovineIdOwner(ownerId);
            if (!bovines.isEmpty()){
                List<Field> fields = fieldRepository.getAllFields();
                for (Bovine bovine:bovines) {
                    for (Field field: fields) {
                        if (bovine.getIdField() == field.getIdField()) {
                            BovineFullInfoDTO bovineDTO = new BovineFullInfoDTO(bovine.getIdBovine(),
                                    bovine.getIdContract(), bovine.getIdOwner(), bovine.getIdField(),
                                    bovine.getSerialNumber(), bovine.getBirthDate(), bovine.getWeight(),
                                    bovine.getHeight(), bovine.getBreed(), bovine.getColor(), bovine.getActive(),
                                    bovine.getObservation(), bovine.getIdBovineParent1(), bovine.getIdBovineParent2(),
                                    bovine.getGender(), field.getAddress(), bovine.getImageCID());
                            bovineDTOList.add(bovineDTO);
                        }
                    }
                }
            }
            return ResponseEntity.ok(bovineDTOList);
        }catch(Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @PostMapping("/")
    @ApiOperation("Create a bovine")
    public ResponseEntity<BovineDTO> createBovine(@RequestBody BovineCreateDTO bovineCreateDTO ) throws Exception {
        try {
            BovineDTO bovineDTO = bovineService.createBovine(bovineCreateDTO);
            if ( bovineDTO.getIdBovine() == 999999 ) {
                return ResponseEntity.status(409).build();
            } else if (bovineDTO.getIdBovine() != 0){
                return ResponseEntity.ok(bovineDTO);
            }
            return ResponseEntity.status(404).build();
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @PutMapping("/{bovineId}")
    @ApiOperation("Update a bovine")
    public ResponseEntity<BovineDTO> updateBovine(@PathVariable long bovineId,
                                                  @RequestBody BovineDTO bovineDTO) throws  Exception {
        try {
            if (bovineId == bovineDTO.getIdBovine()) {
                BovineDTO updatedBovineDTO = bovineService.updateBovine(bovineDTO);
                if (updatedBovineDTO.getIdBovine() != 0) {
                    return ResponseEntity.ok(updatedBovineDTO);
                }
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.status(409).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @DeleteMapping("/{bovineId}")
    @ApiOperation("Delete a bovine")
    public ResponseEntity<String> deleteBovine( @PathVariable long bovineId) throws Exception {
        try {
            if(bovineService.deleteBovine(bovineId)){
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(404).build();
        }catch(Exception e){
            throw new Exception("ERROR: ",e);
        }
    }
}
