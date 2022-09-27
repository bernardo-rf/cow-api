package cow.starter.User;

import cow.starter.Field.models.FieldDTO;
import cow.starter.User.models.*;
import cow.starter.UserType.models.UserType;
import cow.starter.UserType.models.UserTypeRepository;
import cow.starter.utilities.JwtToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@Api("Handles management of COW Users")
@RequestMapping(path = "api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    public UserController() {
        this.userService = new UserService();
    }

    @GetMapping("/{userId}")
    @ApiOperation("Get user by id")
    public ResponseEntity<UserFullInfoDTO> getUser(@PathVariable long userId) throws Exception {
        try {
            User user = userRepository.getUser(userId);
            if (user != null){
                UserFullInfoDTO userInfoDTO = userService.getUserFullInfo(user);
                if (userInfoDTO.getIdUser() != 0) {
                    return ResponseEntity.ok(userInfoDTO);
                }
            }
            return ResponseEntity.status(404).build();
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/email")
    @ApiOperation("Get user by email")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userEmail", required = true, type = "String", example = "bb@mail.pt",
                    value = "user email.")
    })
    public ResponseEntity<UserFullInfoDTO> getUserByEmail(@RequestParam(defaultValue = "") String userEmail)
            throws Exception {
        try {
            User user = userRepository.getUserByEmail(userEmail);
            if (user != null){
                UserFullInfoDTO userInfoDTO = userService.getUserFullInfo(user);
                if (userInfoDTO.getIdUser() != 0) {
                    return ResponseEntity.ok(userInfoDTO);
                }
            }
            return ResponseEntity.status(404).build();
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/wallet")
    @ApiOperation("Get user by id wallet")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idWallet", required = true, type = "long", example = "0.0.48229260",
                    value = "user wallet identifier.")
    })
    public ResponseEntity<UserFullInfoDTO> getUserByIDWallet(@RequestParam(defaultValue = "") String idWallet) throws Exception {
        try {
            User user = userRepository.getUserByIDWallet(idWallet);
            if (user != null){
                UserFullInfoDTO userInfoDTO = userService.getUserFullInfo(user);
                if (userInfoDTO.getIdUser() != 0) {
                    return ResponseEntity.ok(userInfoDTO);
                }
            }
            return ResponseEntity.status(404).build();
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @PostMapping(value = "/auth", consumes = "application/json", produces = "application/json")
    @ApiOperation("Authenticate user")
    public ResponseEntity<UserAuthResponseDTO> authenticateUser(@RequestBody UserAuthDTO userAuthDTO) throws Exception {
        try {
            UserAuthResponseDTO userAuthResponseDTO = userService.authenticate(userAuthDTO);
            if (userAuthResponseDTO.getToken().equals("USER_DISABLE")) {
                return ResponseEntity.status(403).build();
            } else if(userAuthResponseDTO.getToken().equals("")){
                return ResponseEntity.ok(userAuthResponseDTO);
            }
            return ResponseEntity.status(401).build();
        }catch (BadCredentialsException e){
            throw new Exception("ERROR: ", e);
        }
    }

    @PostMapping(value="/", consumes = "application/json", produces = "application/json")
    @ApiOperation("Create a user")
    public ResponseEntity<UserAuthResponseDTO> createUser( @RequestBody UserCreateDTO userCreateDTO )
            throws Exception {
        try {
            UserAuthResponseDTO userAuthResponseDTO = userService.createUser(userCreateDTO);
            if (userAuthResponseDTO.getToken().equals("ERROR_EMAIL")){
                return ResponseEntity.status(403).build();
            }else if (userAuthResponseDTO.getToken().equals("ERROR_USER_TYPE")){
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.ok(userAuthResponseDTO);
        }catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PutMapping(value = "/{userWallet}", consumes = "application/json", produces = "application/json")
    @ApiOperation("Update user")
    public ResponseEntity<UserFullInfoDTO> updateUser(@PathVariable String userWallet,
                                                      @RequestBody UserDTO userDTO) throws Exception {
        try {
            if (userWallet.equals(userDTO.getIdWallet())) {
                UserFullInfoDTO updatedUserDTO = userService.updateUser(userDTO);
                if (updatedUserDTO.getIdUser() != 0) {
                    return ResponseEntity.ok(updatedUserDTO);
                }
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.status(409).build();
        } catch (Exception e) {
            throw new Exception("ERROR:", e);
        }
    }
}
