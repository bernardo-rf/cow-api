/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user.boundary;

import com.bernardo.figueiredo.cow.api.business.user.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "users")
@Api("Management endpoints to handle users")
@CrossOrigin(maxAge = 3600)
@SuppressWarnings("unused")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    public UserController() {
        this.userService = new UserService();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get user by id")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.mapEntityToDTO(user));
    }

    @GetMapping("userType/{userTypeId}")
    @ApiOperation("Get users by user type id")
    public ResponseEntity<List<UserDTO>> getUsersByUserTypeID(long userTypeId) {
        List<User> users = userService.getUsersByUserTypeId(userTypeId);
        return ResponseEntity.ok(userMapper.mapSourceListToTargetList(users));
    }

    @GetMapping("/email")
    @ApiOperation("Get user by email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(userMapper.mapEntityToDTO(user));
    }

    @GetMapping("/wallet")
    @ApiOperation("Get user by wallet id")
    public ResponseEntity<UserDTO> getUserByIDWallet(@RequestParam String walletId) {
        User user = userService.getUserByWalletId(walletId);
        return ResponseEntity.ok(userMapper.mapEntityToDTO(user));
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    @ApiOperation("Create a user")
    public ResponseEntity<UserAuthDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        UserAuth userAuth = userService.createUser(userCreateDTO);
        return ResponseEntity.ok(userMapper.mapEntityToAuthDTO(userAuth));
    }

    @PostMapping(value = "/auth", consumes = "application/json", produces = "application/json")
    @ApiOperation("Authenticate user")
    public ResponseEntity<UserAuthDTO> authenticateUser(@RequestBody UserCredentialDTO userCredentialDTO) {
        UserAuth userAuth = userService.authenticateUser(userCredentialDTO);
        return ResponseEntity.ok(userMapper.mapEntityToAuthDTO(userAuth));
    }

    @PutMapping(value = "/{id}/update", consumes = "application/json", produces = "application/json")
    @ApiOperation("Update user")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO) {
        User user = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(userMapper.mapEntityToDTO(user));
    }
}
