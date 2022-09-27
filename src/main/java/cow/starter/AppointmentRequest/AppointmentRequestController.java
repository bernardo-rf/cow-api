package cow.starter.AppointmentRequest;

import cow.starter.AppointmentRequest.models.AppointmentRequest;
import cow.starter.AppointmentRequest.models.AppointmentRequestCreateDTO;
import cow.starter.AppointmentRequest.models.AppointmentRequestDTO;
import cow.starter.AppointmentRequest.models.AppointmentRequestRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@Api("Handles management of COW Appointment Request")
@RequestMapping(path = "api/appointmentRequest")
public class AppointmentRequestController {

    @Autowired
    private AppointmentRequestService appointmentRequestService;

    @Autowired
    private AppointmentRequestRepository appointmentRequestRepository;

    private List<AppointmentRequestDTO> appointmentRequestDTOList = new ArrayList<>();

    public AppointmentRequestController() {
        appointmentRequestService = new AppointmentRequestService();
    }

    @GetMapping("/{appointmentRequestId}")
    @ApiOperation("Get appointment request by id")
    public ResponseEntity<AppointmentRequestDTO> getAppointmentRequest(@PathVariable long appointmentRequestId)
            throws Exception {
        try {
            AppointmentRequest appointmentRequest = appointmentRequestRepository.getAppointmentRequest(appointmentRequestId);
            if (appointmentRequest != null) {
                AppointmentRequestDTO appointmentRequestDTO = appointmentRequestService.convertToDTO(appointmentRequest);
                return ResponseEntity.ok(appointmentRequestDTO);
            }
            return ResponseEntity.status(404).build();
        }catch(Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/bovine/{bovineId}")
    @ApiOperation("Get all appointments request by id bovine")
    public ResponseEntity<List<AppointmentRequestDTO>> getAppointmentRequestByIDBovine(@PathVariable long bovineId)
            throws Exception {
        try {
            List<AppointmentRequest> appointmentRequestList = appointmentRequestRepository.getAllBovineAppointmentRequest(bovineId);
            if(!appointmentRequestList.isEmpty()){
                appointmentRequestDTOList = appointmentRequestService.getAppointmentsRequest(appointmentRequestList);
            }
            return ResponseEntity.ok(appointmentRequestDTOList);
        }catch(Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("Get all appointments request by id bovine")
    public ResponseEntity<List<AppointmentRequestDTO>> getAppointmentRequestByIDUser(@PathVariable long userId)
            throws Exception {
        try {
            List<AppointmentRequest> appointmentRequestList = appointmentRequestRepository.getAllUserAppointmentRequest(userId);
            if(!appointmentRequestList.isEmpty()){
                appointmentRequestDTOList = appointmentRequestService.getAppointmentsRequest(appointmentRequestList);
            }
            return ResponseEntity.ok(appointmentRequestDTOList);
        }catch(Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/userRequest/{userRequestId}")
    @ApiOperation("Get all appointments request by id bovine")
    public ResponseEntity<List<AppointmentRequestDTO>> getAppointmentRequestByIDUserRequest(@PathVariable long userRequestId)
            throws Exception {
        try {
            List<AppointmentRequest> appointmentRequestList = appointmentRequestRepository.getAllUserRequestAppointmentRequest(userRequestId);
            if(!appointmentRequestList.isEmpty()){
                appointmentRequestDTOList = appointmentRequestService.getAppointmentsRequest(appointmentRequestList);
            }
            return ResponseEntity.ok(appointmentRequestDTOList);
        }catch(Exception e){
            throw new Exception("ERROR: ", e);
        }
    }

    @PostMapping("/")
    @ApiOperation("Create a appointment request")
    public ResponseEntity<AppointmentRequestDTO> createAppointmentRequest(@RequestBody AppointmentRequestCreateDTO appointmentRequestCreateDTO )
            throws Exception {
        try {
            AppointmentRequestDTO appointmentRequestDTO = appointmentRequestService.createAppointmentRequest(appointmentRequestCreateDTO);
            if (appointmentRequestDTO.getIdAppointmentRequest() != 0){
                return ResponseEntity.ok(appointmentRequestDTO);
            }
            return ResponseEntity.status(404).build();
        }catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PutMapping("/{appointmentRequestId}")
    @ApiOperation("Update a field")
    public ResponseEntity<AppointmentRequestDTO> updateAppointmentRequest(@PathVariable long appointmentRequestId,
                                                                          @RequestBody AppointmentRequestDTO appointmentRequestDTO )
            throws Exception {
        try {
            if(appointmentRequestId == appointmentRequestDTO.getIdAppointmentRequest()){
                AppointmentRequestDTO updatedAppointmentRequestDTO = appointmentRequestService.updateAppointmentRequest(appointmentRequestDTO);
                if (updatedAppointmentRequestDTO.getIdAppointmentRequest() != 0){
                    return ResponseEntity.ok(updatedAppointmentRequestDTO);
                }
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.status(409).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @DeleteMapping("/{appointmentRequestId}")
    @ApiOperation("Delete a appointment request")
    public ResponseEntity<String> deleteAppointmentRequest( @PathVariable long appointmentRequestId) throws Exception {
        try {
            if(appointmentRequestService.deleteAppointmentRequest(appointmentRequestId)){
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(404).build();
        }catch(Exception e){
            throw new Exception("ERROR: ",e);
        }
    }
}
