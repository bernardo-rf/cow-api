package hedera.starter.cowBovine;

import com.hedera.hashgraph.sdk.ContractId;
import hedera.starter.cowBovine.models.*;
import hedera.starter.cowField.models.Field;
import hedera.starter.cowField.models.FieldRepository;
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
                BovineDTO bovineDTO = bovineService.convertToDto(bovine);
                bovineDTOList.add(bovineDTO);
            }
            return ResponseEntity.ok(bovineDTOList);
        }catch (Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
    }

    @GetMapping("/{bovineId}")
    @ApiOperation("Get bovine by idBovine")
    public ResponseEntity<BovineDTO> getBovine(@PathVariable long bovineId) throws Exception {
        try {
            Optional<Bovine> bovineAux = bovineRepository.getBovineByIDBovine(bovineId);
            if (bovineAux.isPresent()){
                Bovine bovine = bovineAux.get();
                BovineDTO bovineDTO = bovineService.convertToDto(bovine);
                return ResponseEntity.ok(bovineDTO);
            }
        }catch(Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/{ownerId}/own")
    @ApiOperation("Get bovines by idOwner")
    public ResponseEntity<List<BovineFullInfoDTO>> getOwnBovine(@PathVariable String ownerId) throws Exception {
        try {
            List<BovineFullInfoDTO> bovineDTOList =  new ArrayList<>();
            List<Bovine> bovines = bovineRepository.getBovineByIdUser(ownerId);
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
                                    bovine.getGender(), field.getAddress());
                            bovineDTOList.add(bovineDTO);
                        }
                    }
                }
                return ResponseEntity.ok(bovineDTOList);
            }
        }catch(Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/")
    @ApiOperation("Create a bovine")
    public ResponseEntity<BovineDTO> createBovine(@RequestBody BovineCreateDTO bovineDTO ) throws Exception {
        try {
            if (bovineRepository.checkBovineSerialNumber(bovineDTO.getSerialNumber()).isPresent()){
                return ResponseEntity.status(403).build();
            }

            ContractId contractId = bovineService.createBovine(bovineDTO);
            if (contractId != null){
                Bovine bovine = bovineService.convertToEntity(bovineDTO, contractId.toString());
                bovineRepository.save(bovine);
                BovineDTO newBovineDTO = bovineService.convertToDto(bovine);
                return ResponseEntity.ok(newBovineDTO);
            }
        }catch (Exception e) {
            throw new Exception("INVALID_REGISTER", e);
        }
        return ResponseEntity.status(401).build();
    }

    @PutMapping("/")
    @ApiOperation("Update a bovine")
    public ResponseEntity<BovineDTO> updateBovine(@RequestBody BovineDTO bovineDTO) throws  Exception {
        try {
            Optional<Bovine> oldBovine = bovineRepository.getBovineByIDBovine(bovineDTO.getIdBovine());
            if (oldBovine.isPresent()){
                Bovine updatedBovine = bovineService.updateBovine(oldBovine.get(), bovineDTO);
                Bovine bovineBD = bovineRepository.save(updatedBovine);
                return ResponseEntity.ok(bovineService.convertToDto(bovineBD));
            }
        }catch (Exception e) {
            throw new Exception("INVALID_REGISTER", e);
        }
        return ResponseEntity.status(401).build();
    }

    @DeleteMapping("/{idBovine}")
    @ApiOperation("Delete a bovine")
    public ResponseEntity<String> deleteBovine( @PathVariable long idBovine) throws Exception {
        try{
            Bovine bovineToDelete = bovineRepository.getBovineByIDBovine(idBovine).get();
            bovineService.deleteBovine(bovineToDelete.getIdContract());
            bovineRepository.deleteById(idBovine);
            return ResponseEntity.status(201).build();
        }catch(Exception e){
            throw new Exception("INVALID_REGISTER", e);
        }
    }
}
