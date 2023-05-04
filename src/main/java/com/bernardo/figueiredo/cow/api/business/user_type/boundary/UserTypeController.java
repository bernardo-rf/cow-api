/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user_type.boundary;

import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 3600)
@Api("Handles management of COW UserTypes")
@RequestMapping(path = "api/userTypes")
public class UserTypeController {

    @Autowired
    private UserTypeRepository userTypeRepository;

    public UserTypeController() {}

    @GetMapping("/")
    @ApiOperation("Get all user types")
    public List<UserType> getUserTypes() {
        return userTypeRepository.getAllUserTypes();
    }

    @GetMapping("/{userTypeId}")
    @ApiOperation("Get user type by user type id")
    @ApiImplicitParams({
        @ApiImplicitParam(
                name = "userTypeId",
                required = true,
                type = "long",
                example = "1",
                value = "user type identifier.")
    })
    public Optional<UserType> getUserType(@PathVariable long userTypeId) {
        return userTypeRepository.findById(userTypeId);
    }

    @PostMapping("/")
    @ApiOperation("Create a user type")
    @ApiImplicitParams({
        @ApiImplicitParam(
                name = "userTypeDescription",
                type = "string",
                example = "Farmer",
                value = "define the description of the user type."),
        @ApiImplicitParam(
                name = "active",
                type = "boolean",
                example = "1",
                value = "define the active of the user type.")
    })
    public UserType createUserType(
            @RequestParam(defaultValue = "Farmer") String userTypeDescription,
            @RequestParam(defaultValue = "1") Boolean active) {
        UserType newUserType = new UserType(userTypeDescription, active);
        return userTypeRepository.save(newUserType);
    }
}
