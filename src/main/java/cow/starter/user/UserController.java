package cow.starter.User;

import cow.starter.User.models.*;
import cow.starter.UserType.models.UserTypeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    @GetMapping("/")
    @ApiOperation("Get all users")
    public ResponseEntity<List<UserFullInfoDTO>> getAllUsers() throws Exception {
        try {
            List<User> users = userRepository.getAllUsers();
            List<UserFullInfoDTO> userFullInfoDTOS = new ArrayList<>();
            if (!users.isEmpty()) {
                for (User user: users) {
                    UserFullInfoDTO userInfoDTO = userService.getUserFullInfo(user.getIdUser());
                    if (userInfoDTO.getIdUser() != 0) {
                        userFullInfoDTOS.add(userInfoDTO);
                    }
                }
            }
            return ResponseEntity.ok(userFullInfoDTOS);
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/veterinary")
    @ApiOperation("Get all veterinaries")
    public ResponseEntity<List<UserFullInfoDTO>> getAllUsersVeterinary() throws Exception {
        try {
            List<User> users = userRepository.getAllUsersVeterinary();
            List<UserFullInfoDTO> userFullInfoDTOS = new ArrayList<>();
            if (!users.isEmpty()) {
                for (User user: users) {
                    UserFullInfoDTO userInfoDTO = userService.getUserFullInfo(user.getIdUser());
                    if (userInfoDTO.getIdUser() != 0) {
                        userFullInfoDTOS.add(userInfoDTO);
                    }
                }
            }
            return ResponseEntity.ok(userFullInfoDTOS);
        }catch (Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/{userId}")
    @ApiOperation("Get user by id")
    public ResponseEntity<UserFullInfoDTO> getUser(@PathVariable long userId) throws Exception {
        try {
            UserFullInfoDTO userInfoDTO = userService.getUserFullInfo(userId);
            if (userInfoDTO.getIdUser() != 0) {
                return ResponseEntity.ok(userInfoDTO);
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
            User user = userRepository.getUserByEmail(userEmail, 0);
            if (user != null){
                UserFullInfoDTO userInfoDTO = userService.getUserFullInfo(user.getIdUser());
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
                UserFullInfoDTO userInfoDTO = userService.getUserFullInfo(user.getIdUser());
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
            if (userAuthResponseDTO.getToken() == null){
                return ResponseEntity.status(401).build();
            }else if (userAuthResponseDTO.getToken().equals("USER_DISABLE")) {
                return ResponseEntity.status(401).build();
            } else if(!userAuthResponseDTO.getToken().equals("")){
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
                return ResponseEntity.status(409).build();
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
            UserFullInfoDTO updatedUserDTO = userService.updateUser(userDTO);

            if(updatedUserDTO.getName().equals("error_password_equals_to_previous")){
                return ResponseEntity.status(406).build();
            }else if(updatedUserDTO.getName().equals("error_email_already_taken")) {
                return ResponseEntity.status(409).build();
            }

             return ResponseEntity.ok(updatedUserDTO);
        } catch (Exception e) {
            throw new Exception("ERROR:", e);
        }
    }
}
