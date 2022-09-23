package hedera.starter.cowUser;

import hedera.starter.cowUser.models.*;
import hedera.starter.cowUserType.models.UserType;
import hedera.starter.cowUserType.models.UserTypeRepository;
import hedera.starter.utilities.JwtToken;
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

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserController() {
        this.userService = new UserService();
    }

    @GetMapping("/")
    @ApiOperation("Get all users")
    public ResponseEntity<List<UserFullInfoDTO>> getAllUsers() throws Exception {
        try {
            List<UserFullInfoDTO> userFullInfoDTOList =  new ArrayList<>();
            List<User> users = userRepository.getAllUsers();
            if (users.isEmpty()){
                return ResponseEntity.ok(userFullInfoDTOList);
            }
            for (User user:users) {
                Optional<UserType> userType = userTypeRepository.getUserTypeByIDUserType(user.getIdUserType());
                if(userType.isPresent()){
                    UserFullInfoDTO userFullInfoDTO = userService.convertToDTO(user, userType.get().getDescription().toUpperCase());
                    userFullInfoDTOList.add(userFullInfoDTO);
                }
            }
            return ResponseEntity.ok(userFullInfoDTOList);
        }catch (Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
    }

    @GetMapping("/{userID}")
    @ApiOperation("Get user by id")
    public ResponseEntity<UserFullInfoDTO> getUser(@PathVariable long userID) throws Exception {
        try {
            Optional<User> userAux = userRepository.findById(userID);
            if (userAux.isPresent()){
                Optional<UserType> userType = userTypeRepository.getUserTypeByIDUserType(userAux.get().getIdUserType());
                if(userType.isPresent()) {
                    return ResponseEntity.ok(userService.getUserDTO(userAux.get(), userType.get()));
                }
            }
        }catch (Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/email")
    @ApiOperation("Get user by email")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userEmail", required = true, type = "String", example = "bb@mail.pt",
                    value = "user email.")
    })
    public ResponseEntity<UserFullInfoDTO> getUserByEmail(@RequestParam(defaultValue = "") String userEmail) throws Exception {
        try {
            Optional<User> userAux = userRepository.findUserByEmail(userEmail);
            if (userAux.isPresent()){
                Optional<UserType> userType = userTypeRepository.getUserTypeByIDUserType(userAux.get().getIdUserType());
                if(userType.isPresent()) {
                    return ResponseEntity.ok(userService.getUserDTO(userAux.get(), userType.get()));
                }
            }
        }catch (Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/wallet")
    @ApiOperation("Get user by id wallet")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idWallet", required = true, type = "long", example = "0.0.48229260",
                    value = "user wallet identifier.")
    })
    public ResponseEntity<UserFullInfoDTO> getUserByIDWallet(@RequestParam(defaultValue = "") String idWallet) throws Exception {
        try {
            Optional<User> userAux = userRepository.findUserByIDWallet(idWallet);
            if (userAux.isPresent()){
                Optional<UserType> userType = userTypeRepository.getUserTypeByIDUserType(userAux.get().getIdUserType());
                if(userType.isPresent()) {
                    return ResponseEntity.ok(userService.getUserDTO(userAux.get(), userType.get()));
                }
            }
        }catch (Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping(value = "/auth", consumes = "application/json", produces = "application/json")
    @ApiOperation("Authenticate user")
    public ResponseEntity<UserAuthResponseDTO> authenticateUser(@RequestBody UserAuthDTO userAuthDTO) throws Exception {
        try {
            Optional<User> userLogin = userRepository.findUserByEmail(userAuthDTO.getEmail());
            if (userLogin.isPresent() && bCryptPasswordEncoder.matches(userAuthDTO.getPassword(), userLogin.get().getPassword())){
                if (!userLogin.get().getActive()) {
                    return ResponseEntity.status(403).build();
                }

                Optional<UserType> userType = userTypeRepository.getUserTypeByIDUserType(userLogin.get().getIdUserType());
                if(userType.isPresent()){
                    UserAuthResponseDTO userAuthResponseDTO = userService.authenticate(userLogin.get(), userType.get(),
                            jwtToken);
                    if (!userAuthResponseDTO.getToken().isEmpty()){
                        return ResponseEntity.ok(userAuthResponseDTO);
                    }
                }
            }
        }catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping(value="/", consumes = "application/json", produces = "application/json")
    @ApiOperation("Create a user")
    public ResponseEntity<UserAuthResponseDTO> createUser( @RequestBody UserCreateDTO userCreateDTO )
            throws Exception {
        try {
            if (userRepository.findUserByEmail(userCreateDTO.getEmail()).isPresent()){
                return ResponseEntity.status(403).build();
            }

            List<String> userObject = userService.createUser(userCreateDTO.getIdUserType(), userCreateDTO.getName(),
                    new Date(3810, Calendar.JANUARY, 1),  userCreateDTO.getEmail(),
                    userCreateDTO.getPassword(), true);

            if (!userObject.isEmpty()){
                Optional<UserType> userType = userTypeRepository.getUserTypeByIDUserType(userCreateDTO.getIdUserType());
                if(userType.isPresent()){
                    String balance = userObject.get(2).substring(0, userObject.get(2).length()-3);
                    User user = new User(userObject.get(0), userObject.get(1), userCreateDTO.getIdUserType(),
                            userCreateDTO.getName(), new Date(3810, Calendar.JANUARY, 1),
                            userCreateDTO.getEmail(), bCryptPasswordEncoder.encode(userCreateDTO.getPassword()),
                            true, Double.valueOf(balance));
                    userRepository.save(user);

                    UserAuthResponseDTO userAuthResponseDTO = userService.authenticate(user, userType.get(),
                            jwtToken);
                    if (!userAuthResponseDTO.getToken().isEmpty()){
                        return ResponseEntity.ok(userAuthResponseDTO);
                    }
                }
            }
        }catch (Exception e) {
            throw new Exception("INVALID_REGISTER", e);
        }
        return ResponseEntity.status(401).build();
    }

    @PutMapping(value = "/{userWallet}", consumes = "application/json", produces = "application/json")
    @ApiOperation("Update user")
    public ResponseEntity<UserFullInfoDTO> updateUser(@PathVariable String userWallet,
                                                      @RequestBody UserFullInfoDTO userFullInfoDTO) throws Exception {
        try{
            if(userWallet.equals(userFullInfoDTO.getIdWallet())){
                User oldUser = userRepository.findUserByIDWallet(userWallet).get();
                User newUser = userService.updateUser(oldUser, userFullInfoDTO);
                userRepository.save(newUser);
                Optional<UserType> userType = userTypeRepository.getUserTypeByIDUserType(newUser.getIdUserType());
                return ResponseEntity.ok(userService.convertToDTO(newUser,
                        userType.get().getDescription().toUpperCase()));
            }
        }catch  (Exception e){
            throw new Exception("INVALID_OPERATION", e);
        }
        return ResponseEntity.status(401).build();
    }
}
