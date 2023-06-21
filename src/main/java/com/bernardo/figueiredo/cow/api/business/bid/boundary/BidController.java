/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.bid.boundary;

import com.bernardo.figueiredo.cow.api.business.bid.dto.Bid;
import com.bernardo.figueiredo.cow.api.business.bid.dto.BidCreateDTO;
import com.bernardo.figueiredo.cow.api.business.bid.dto.BidDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "bids")
@CrossOrigin(maxAge = 3600)
@Api("Management endpoints to handle bids")
@SuppressWarnings("unused")
public class BidController {

    @Autowired
    private final BidService bidService;

    @Autowired
    private BidMapper bidMapper;

    public BidController() {
        bidService = new BidService();
    }

    @GetMapping("/")
    @ApiOperation("Get bids")
    public ResponseEntity<List<BidDTO>> getBids() {
        List<Bid> bids = bidService.getBids();
        return ResponseEntity.ok(bidMapper.mapSourceListToTargetList(bids));
    }

    @GetMapping("/{id}")
    @ApiOperation("Get bid by id")
    public ResponseEntity<BidDTO> getBidById(@PathVariable long id) {
        Bid bid = bidService.getBidById(id);
        return ResponseEntity.ok(bidMapper.mapEntityToDTO(bid));
    }

    @PostMapping("/")
    @ApiOperation("Create bid")
    public ResponseEntity<BidDTO> createBid(@RequestBody BidCreateDTO bidCreateDTO) {
        Bid bid = bidService.createBid(bidCreateDTO);
        return ResponseEntity.ok(bidMapper.mapEntityToDTO(bid));
    }
}
