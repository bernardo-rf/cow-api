/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointment.boundary;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentCreateDTO;
import com.bernardo.figueiredo.cow.api.business.appointment.dto.AppointmentDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "appointments")
@Api("Management endpoints to handle appointments")
@CrossOrigin(maxAge = 3600)
@SuppressWarnings("unused")
public class AppointmentController {
    @Autowired
    private final AppointmentService appointmentService;

    @Autowired
    private AppointmentMapper appointmentMapper;

    public AppointmentController() {
        this.appointmentService = new AppointmentService();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get appointment by id")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentMapper.mapEntityToDTO(appointment));
    }

    @GetMapping("/bovine/{id}")
    @ApiOperation("Get appointments by bovine id")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByBovineId(@PathVariable long id) {

        List<Appointment> appointments = appointmentService.getAppointmentsByBovineId(id);
        return ResponseEntity.ok(appointmentMapper.mapSourceListToTargetList(appointments));
    }

    @GetMapping("/user/{id}")
    @ApiOperation("Get appointments by user id")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentByUserId(@PathVariable long id) {
        List<Appointment> appointments = appointmentService.getAppointmentsByUserId(id);
        return ResponseEntity.ok(appointmentMapper.mapSourceListToTargetList(appointments));
    }

    @PostMapping("/")
    @ApiOperation("Create appointment")
    public ResponseEntity<List<AppointmentDTO>> createAppointment(
            @RequestBody AppointmentCreateDTO appointmentCreateDTO) {

        List<Appointment> appointments = appointmentService.createAppointment(appointmentCreateDTO);
        return ResponseEntity.ok(appointmentMapper.mapSourceListToTargetList(appointments));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update appointment")
    public ResponseEntity<AppointmentDTO> updateAppointment(
            @PathVariable long id, @RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentService.updateAppointment(id, appointmentDTO);
        return ResponseEntity.ok(appointmentMapper.mapEntityToDTO(appointment));
    }

    @PutMapping("/{id}/status")
    @ApiOperation("Update appointment status")
    public ResponseEntity<AppointmentDTO> updateAppointmentStatus(@PathVariable long id, @RequestParam int status) {
        Appointment appointment = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(appointmentMapper.mapEntityToDTO(appointment));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete appointment")
    public ResponseEntity<String> deleteAppointment(@PathVariable long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted with success.");
    }
}
