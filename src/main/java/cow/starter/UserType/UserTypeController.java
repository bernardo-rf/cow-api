package cow.starter.UserType;

import cow.starter.UserType.models.UserType;
import cow.starter.UserType.models.UserTypeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
@Api("Handles management of COW UserTypes")
@RequestMapping(path = "api/userTypes")
public class UserTypeController {

    @Autowired
    private UserTypeRepository userTypeRepository;

    public UserTypeController() { }

    @GetMapping("/")
    @ApiOperation("Get all user types")
    public List<UserType> getUserTypes() {
        return userTypeRepository.getAllUserTypes();
    }

    @GetMapping("/{idUserType}")
    @ApiOperation("Get user type by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idUserType", required = true, type = "long", example = "1",
                    value = "user type identifier.")
    })
    public Optional<UserType> getUserType(@PathVariable long idUserType) {
        return userTypeRepository.findById(idUserType);
    }

    @PostMapping("/")
    @ApiOperation("Create a user type")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "userTypeDescription",type = "string", example = "Farmer",
                    value = "define the description of the user type."),
            @ApiImplicitParam( name = "active", type = "boolean", example = "1",
                    value = "define the active of the user type.")
    })
    public UserType createField(@RequestParam(defaultValue = "Farmer") String userTypeDescription,
                                @RequestParam(defaultValue = "1") Boolean active) {
        UserType newUserType = new UserType(userTypeDescription, active );
        return userTypeRepository.save(newUserType);
    }
}
