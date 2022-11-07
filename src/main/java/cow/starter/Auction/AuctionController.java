package cow.starter.Auction;

import cow.starter.Auction.models.AuctionCreateDTO;
import cow.starter.Auction.models.AuctionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@Api("Handles management of COW Auction")
@RequestMapping(path = "api/auctions")
public class AuctionController {

    @Autowired
    AuctionService auctionService;

    public AuctionController() {
        auctionService = new AuctionService();
    }

    @GetMapping("/")
    @ApiOperation("Get all auctions")
    public ResponseEntity<List<AuctionDTO>> getAllAuctions() throws Exception {
        try {
            return ResponseEntity.ok(auctionService.getAllAuctions());
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{auctionID}")
    @ApiOperation("Get auction by auction id")
    public ResponseEntity<AuctionDTO> getAllBovinesByIDOwnerMale(@PathVariable long auctionID) throws Exception {
        try {
            AuctionDTO auctionDTO =  auctionService.getAuction(auctionID);
            if (auctionDTO.getIdAuction() == 0){
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.ok(auctionDTO);
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PostMapping("/")
    @ApiOperation("Create a auction")
    public ResponseEntity<AuctionDTO> createBovine(@RequestBody AuctionCreateDTO auctionCreateDTO) throws Exception {
        try {
            AuctionDTO auctionDTO = auctionService.createAuction(auctionCreateDTO);
            if (auctionDTO.getIdAuction() == 999999) {
                return ResponseEntity.status(409).build();
            } else if (auctionDTO.getIdAuction() == 0) {
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.ok(auctionDTO);
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PutMapping("/{auctionID}")
    @ApiOperation("Update a auction")
    public ResponseEntity<AuctionDTO> updateAuction(@PathVariable long auctionID,
                                                    @RequestBody AuctionDTO auctionDTO) throws Exception {
        try {
            if (auctionID != auctionDTO.getIdAuction()) {
                return ResponseEntity.status(409).build();
            }

            AuctionDTO updatedAuctionDTO = auctionService.updateAuction(auctionDTO);
            if (updatedAuctionDTO.getIdAuction() == 999999) {
                return ResponseEntity.status(409).build();
            } else if (updatedAuctionDTO.getIdAuction() == 0) {
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.ok(updatedAuctionDTO);
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }
}
