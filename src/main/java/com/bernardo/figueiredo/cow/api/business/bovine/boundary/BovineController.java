/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.bovine.boundary;

import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.BovineCreateDTO;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.BovineDTO;
import io.swagger.annotations.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "bovines")
@CrossOrigin(maxAge = 3600)
@Api("Management endpoints to handle bovines")
@SuppressWarnings("unused")
public class BovineController {
    @Autowired
    BovineService bovineService;

    @Autowired
    private BovineMapper bovineMapper;

    public BovineController() {
        bovineService = new BovineService();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get bovine by id")
    public ResponseEntity<BovineDTO> getBovine(@PathVariable long id) {
        Bovine bovine = bovineService.getBovineById(id);
        return ResponseEntity.ok(bovineMapper.mapEntityToDTO(bovine));
    }

    @GetMapping("/")
    @ApiOperation("Get bovines")
    public ResponseEntity<List<BovineDTO>> getBovinesActive() {
        List<Bovine> bovines = bovineService.getBovinesActive();
        return ResponseEntity.ok(bovineMapper.mapSourceListToTargetList(bovines));
    }

    @GetMapping("/genealogy/{id}")
    @ApiOperation("Get bovine genealogy by id")
    public ResponseEntity<List<BovineDTO>> getBovineGenealogyById(@PathVariable long id) {
        List<Bovine> bovines = bovineService.getBovineGenealogyById(id);
        return ResponseEntity.ok(bovineMapper.mapSourceListToTargetList(bovines));
    }

    @GetMapping("/{userWalletId}")
    @ApiOperation("Get bovines by user wallet id")
    public ResponseEntity<List<BovineDTO>> getBovinesByUserWalletId(@PathVariable String userWalletId) {
        List<Bovine> bovines = bovineService.getBovinesByUserWalletId(userWalletId);
        return ResponseEntity.ok(bovineMapper.mapSourceListToTargetList(bovines));
    }

    @GetMapping("/{userWalletId}/auction")
    @ApiOperation("Get bovines to auction by user wallet id")
    public ResponseEntity<List<BovineDTO>> getBovinesToAuction(@PathVariable String userWalletId) {
        List<Bovine> bovines = bovineService.getBovineToAuctionByUserWalletId(userWalletId);
        return ResponseEntity.ok(bovineMapper.mapSourceListToTargetList(bovines));
    }

    @PostMapping("/")
    @ApiOperation("Create bovine")
    public ResponseEntity<BovineDTO> createBovine(@RequestBody BovineCreateDTO bovineCreateDTO) {
        Bovine bovine = bovineService.createBovine(bovineCreateDTO);
        return ResponseEntity.ok(bovineMapper.mapEntityToDTO(bovine));
    }

    // TODO
    //    @PutMapping("/{id}")
    //    @ApiOperation("Update bovine")
    //    public ResponseEntity<BovineDTO> updateBovine(@PathVariable long id, @RequestBody BovineDTO bovineDTO) {
    //        Bovine bovine = bovineService.updateBovine(id, bovineDTO);
    //        return ResponseEntity.ok(bovineMapper.mapEntityToDTO(bovine));
    //    }

    // TODO
    //    @DeleteMapping("/{id}")
    //    @ApiOperation("Delete bovine")
    //    public ResponseEntity<String> deleteBovine(@PathVariable long id) {
    //        bovineService.deleteBovine(id);
    //        return ResponseEntity.ok("Bovine deleted with success.");
    //    }
}
