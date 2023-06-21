/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.auction.boundary;

import com.bernardo.figueiredo.cow.api.business.auction.dto.Auction;
import com.bernardo.figueiredo.cow.api.business.auction.dto.AuctionCreateDTO;
import com.bernardo.figueiredo.cow.api.business.auction.dto.AuctionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "auctions")
@CrossOrigin(maxAge = 3600)
@Api("Management endpoints to handle auctions")
@SuppressWarnings("unused")
public class AuctionController {

    @Autowired
    private final AuctionService auctionService;

    @Autowired
    private AuctionMapper auctionMapper;

    public AuctionController() {
        auctionService = new AuctionService();
    }

    @GetMapping("/")
    @ApiOperation("Get auctions")
    public ResponseEntity<List<AuctionDTO>> getAuctions() {
        List<Auction> auctions = auctionService.getAuctions();
        return ResponseEntity.ok(auctionMapper.mapSourceListToTargetList(auctions));
    }

    @GetMapping("/{id}")
    @ApiOperation("Get auction by auction id")
    public ResponseEntity<AuctionDTO> getAuctionByAuctionId(@PathVariable long id) {
        Auction auction = auctionService.getAuctionById(id);
        return ResponseEntity.ok(auctionMapper.mapEntityToDTO(auction));
    }

    @PostMapping("/")
    @ApiOperation("Create a auction")
    public ResponseEntity<AuctionDTO> createBovine(@RequestBody AuctionCreateDTO auctionCreateDTO) {
        Auction auction = auctionService.createAuction(auctionCreateDTO);
        return ResponseEntity.ok(auctionMapper.mapEntityToDTO(auction));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update a auction")
    public ResponseEntity<AuctionDTO> updateAuction(@PathVariable long id, @RequestBody AuctionDTO auctionDTO) {
        Auction auction = auctionService.updateAuction(id, auctionDTO);
        return ResponseEntity.ok(auctionMapper.mapEntityToDTO(auction));
    }

    @PutMapping("/{id}/status")
    @ApiOperation("Update a auction status")
    public ResponseEntity<AuctionDTO> updateAuctionStatus(@PathVariable long id, @RequestParam int status) {
        Auction auction = auctionService.updateAuctionStatus(id, status);
        return ResponseEntity.ok(auctionMapper.mapEntityToDTO(auction));
    }
}
