package hedera.starter.cowBovine;

import hedera.starter.cowBovine.models.Bovine;
import hedera.starter.cowBovine.models.BovineDTO;
import hedera.starter.cowBovine.models.BovineRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@Api("Handles management of COW Bovine")
@RequestMapping(path = "api/bovines")
public class BovineController {
    @Autowired
    BovineService bovineService;

    @Autowired
    BovineRepository bovineRepository;

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
    public ResponseEntity<List<BovineDTO>> getOwnBovine(@PathVariable String ownerId) throws Exception {
        try {
            List<BovineDTO> bovineDTOList =  new ArrayList<>();
            List<Bovine> bovines = bovineRepository.getBovineByIdUser(ownerId);
            if (!bovines.isEmpty()){
                for (Bovine bovine:bovines) {
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
}
