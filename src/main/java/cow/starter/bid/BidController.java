/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.bid;

import cow.starter.bid.models.BidCreateDTO;
import cow.starter.bid.models.BidDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
@Api("Handles management of COW Bid")
@RequestMapping(path = "api/bids")
public class BidController {

    @Autowired
    BidService bidService;

    public BidController() {
        bidService = new BidService();
    }

    @GetMapping("/")
    @ApiOperation("Get all bids")
    public ResponseEntity<List<BidDTO>> getAllBids() throws Exception {
        try {
            return ResponseEntity.ok(bidService.getAllBids());
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{bidID}")
    @ApiOperation("Get bid by bid id")
    public ResponseEntity<BidDTO> getBidByIDBid(@PathVariable long bidID) throws Exception {
        try {
            BidDTO bidDTO =  bidService.getBid(bidID);
            if (bidDTO.getIdBid() == 0){
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.ok(bidDTO);
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PostMapping("/")
    @ApiOperation("Create a bid")
    public ResponseEntity<BidDTO> createBovine(@RequestBody BidCreateDTO bidCreateDTO) throws Exception {
        try {
            BidDTO bidDTO = bidService.createBid(bidCreateDTO);
            if (bidDTO.getIdBid() == 999999) {
                return ResponseEntity.status(409).build();
            } else if (bidDTO.getIdBid() == 0) {
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.ok(bidDTO);
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }
}
