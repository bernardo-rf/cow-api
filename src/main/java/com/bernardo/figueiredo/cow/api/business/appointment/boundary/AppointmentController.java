/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointment.boundary;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentCreateDTO;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentDTO;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentFullInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 3600)
@Api("Handles management of COW Appointment")
@RequestMapping(path = "api/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public AppointmentController() {
        appointmentService = new AppointmentService();
    }

    @GetMapping("/{appointmentId}")
    @ApiOperation("Get appointment by id")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable long appointmentId) throws Exception {
        try {
            Appointment appointment = appointmentRepository.getAppointment(appointmentId);
            if (appointment != null) {
                AppointmentDTO appointmentDTO = appointmentService.convertToDTO(appointment);
                return ResponseEntity.ok(appointmentDTO);
            }
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/bovine/{bovineId}")
    @ApiOperation("Get all appointments by id bovine")
    public ResponseEntity<List<AppointmentFullInfoDTO>> getAppointmentsByIDBovine(@PathVariable long bovineId)
            throws Exception {
        try {
            List<AppointmentFullInfoDTO> appointmentDTOList = new ArrayList<>();
            List<Appointment> appointmentList = appointmentRepository.getAllBovineAppointment(bovineId);
            if (!appointmentList.isEmpty()) {
                appointmentDTOList = appointmentService.getAppointments(appointmentList);
            }
            return ResponseEntity.ok(appointmentDTOList);
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/bovines/{userWallet}")
    @ApiOperation("Get all appointments of my cows")
    public ResponseEntity<List<AppointmentFullInfoDTO>> getAppointmentsOfOwnedCows(@PathVariable String userWallet)
            throws Exception {
        try {
            List<Appointment> appointmentList = appointmentRepository.getAllOwnedBovineAppointment(userWallet);
            List<AppointmentFullInfoDTO> appointmentDTOList = new ArrayList<>();
            if (!appointmentList.isEmpty()) {
                appointmentDTOList = appointmentService.getAppointments(appointmentList);
            }
            return ResponseEntity.ok(appointmentDTOList);
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("Get all appointments by id user")
    public ResponseEntity<List<AppointmentFullInfoDTO>> getAppointmentByIDUser(@PathVariable long userId)
            throws Exception {
        try {
            List<Appointment> appointments = appointmentRepository.getAllUserAppointment(userId);
            List<AppointmentFullInfoDTO> appointmentDTOList = new ArrayList<>();
            if (!appointments.isEmpty()) {
                appointmentDTOList = appointmentService.getAppointments(appointments);
            }
            return ResponseEntity.ok(appointmentDTOList);
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PostMapping("/")
    @ApiOperation("Create a appointment")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentCreateDTO appointmentCreateDTO)
            throws Exception {
        try {
            AppointmentDTO appointmentDTO = appointmentService.createAppointment(appointmentCreateDTO);
            if (appointmentDTO.getIdAppointment() != 0) {
                return ResponseEntity.ok(appointmentDTO);
            }
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PutMapping("/{appointmentId}")
    @ApiOperation("Update a appointment")
    public ResponseEntity<AppointmentDTO> updateAppointment(
            @PathVariable long appointmentId, @RequestBody AppointmentDTO appointmentDTO) throws Exception {
        try {
            if (appointmentId == appointmentDTO.getIdAppointment()) {
                AppointmentDTO updatedAppointmentDTO = appointmentService.updateAppointment(appointmentDTO);
                if (updatedAppointmentDTO.getIdAppointment() != 0) {
                    return ResponseEntity.ok(updatedAppointmentDTO);
                }
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.status(409).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @PutMapping("/{appointmentId}/status")
    @ApiOperation("Update a appointment status")
    public ResponseEntity<AppointmentDTO> updateAppointmentRequest(
            @PathVariable long appointmentId, @RequestParam int status) throws Exception {
        try {
            AppointmentDTO updatedAppointmentDTO = appointmentService.updateAppointmentStatus(appointmentId, status);
            if (updatedAppointmentDTO.getIdAppointment() != 0) {
                return ResponseEntity.ok(updatedAppointmentDTO);
            }
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }

    @DeleteMapping("/{appointmentId}")
    @ApiOperation("Delete a appointment")
    public ResponseEntity<String> deleteAppointmentRequest(@PathVariable long appointmentId) throws Exception {
        try {
            if (appointmentService.deleteAppointment(appointmentId)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            throw new Exception("ERROR: ", e);
        }
    }
}
