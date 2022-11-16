package cow.starter.Bovine;

import cow.starter.Bovine.models.*;
import cow.starter.Field.models.FieldRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin(maxAge = 3600)
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
            return ResponseEntity.ok(bovineService.getAllBovine());
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/genealogy/{bovineId}")
    @ApiOperation("Get all genealogy by idBovine")
    public ResponseEntity<List<BovineDTO>> getGenealogy(@PathVariable long bovineId) throws Exception {
        try {
            return ResponseEntity.ok(bovineService.getGenealogy(bovineId));
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{bovineId}")
    @ApiOperation("Get bovine by idBovine")
    public ResponseEntity<BovineFullInfoDTO> getBovine(@PathVariable long bovineId) throws Exception {
        try {
            BovineFullInfoDTO bovineFullInfoDTO = bovineService.getBovine(bovineId);
            if (bovineFullInfoDTO.getIdBovine() == 0){
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.ok(bovineFullInfoDTO);
        }catch(Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{ownerId}/own")
    @ApiOperation("Get bovines by idOwner")
    public ResponseEntity<List<BovineFullInfoDTO>> getOwnedBovines(@PathVariable String ownerId) throws Exception {
        try {
            return ResponseEntity.ok( bovineService.getAllBovineOwned(ownerId));
        }catch(Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{ownerId}/own/auction")
    @ApiOperation("Get bovines by idOwner")
    public ResponseEntity<List<BovineFullInfoDTO>> getBovinesToAuction(@PathVariable String ownerId) throws Exception {
        try {
            return ResponseEntity.ok( bovineService.getAllBovineOwnedToAuction(ownerId));
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
