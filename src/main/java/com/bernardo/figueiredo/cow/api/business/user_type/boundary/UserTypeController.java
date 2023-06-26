/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user_type.boundary;

import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserType;
import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserTypeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "user_types")
@Api("Management endpoints to handle user types")
@CrossOrigin(maxAge = 3600)
@SuppressWarnings("unused")
public class UserTypeController {

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private UserTypeMapper userTypeMapper;

    @GetMapping("/")
    @ApiOperation("Get user types")
    public ResponseEntity<List<UserTypeDTO>> getUserTypes() {
        List<UserType> userTypes = userTypeService.getUserTypes();
        return ResponseEntity.ok(userTypeMapper.mapSourceListToTargetList(userTypes));
    }
}
