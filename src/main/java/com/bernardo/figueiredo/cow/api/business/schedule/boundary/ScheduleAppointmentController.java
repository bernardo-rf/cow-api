/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.schedule.boundary;

import com.bernardo.figueiredo.cow.api.business.schedule.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 3600)
@Api("Handles management of COW Schedule")
@RequestMapping(path = "schedule-appointments")
@SuppressWarnings("unused")
public class ScheduleAppointmentController {

    @Autowired
    private ScheduleAppointmentService scheduleAppointmentService;

    @Autowired
    private ScheduleAppointmentMapper scheduleAppointmentMapper;

    public ScheduleAppointmentController() {
        scheduleAppointmentService = new ScheduleAppointmentService();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get schedule appointment by id")
    public ResponseEntity<ScheduleAppointmentDTO> getScheduleAppointmentById(@PathVariable long id) {
        ScheduleAppointment scheduleAppointment = scheduleAppointmentService.getScheduleAppointmentById(id);
        return ResponseEntity.ok(scheduleAppointmentMapper.mapEntityToDTO(scheduleAppointment));
    }

    @GetMapping("/bovine/{id}")
    @ApiOperation("Get schedule appointments by bovine id")
    public ResponseEntity<List<ScheduleAppointmentDTO>> getScheduleAppointmentByBovineId(@PathVariable long id) {
        List<ScheduleAppointment> scheduleAppointments =
                scheduleAppointmentService.getScheduleAppointmentByBovineId(id);
        return ResponseEntity.ok(scheduleAppointmentMapper.mapSourceListToTargetList(scheduleAppointments));
    }

    @GetMapping("/veterinary/{id}")
    @ApiOperation("Get schedule appointments by user id (veterinary)")
    public ResponseEntity<List<ScheduleAppointmentDTO>> getScheduleAppointmentByVeterinaryId(@PathVariable long id) {
        List<ScheduleAppointment> scheduleAppointments =
                scheduleAppointmentService.getScheduleAppointmentByVeterinaryId(id);
        return ResponseEntity.ok(scheduleAppointmentMapper.mapSourceListToTargetList(scheduleAppointments));
    }

    @GetMapping("/owner/{id}")
    @ApiOperation("Get schedule appointments by user id (owner)")
    public ResponseEntity<List<ScheduleAppointmentDTO>> getScheduleAppointmentByOwnerId(@PathVariable long id) {
        List<ScheduleAppointment> scheduleAppointments = scheduleAppointmentService.getScheduleAppointmentByOwnerId(id);
        return ResponseEntity.ok(scheduleAppointmentMapper.mapSourceListToTargetList(scheduleAppointments));
    }

    @PostMapping("/")
    @ApiOperation("Create schedule appointment")
    public ResponseEntity<ScheduleAppointmentDTO> createScheduleAppointment(
            @RequestBody ScheduleAppointmentCreateDTO scheduleAppointmentCreateDTO) {
        ScheduleAppointment scheduleAppointment =
                scheduleAppointmentService.createScheduleAppointment(scheduleAppointmentCreateDTO);
        return ResponseEntity.ok(scheduleAppointmentMapper.mapEntityToDTO(scheduleAppointment));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update schedule")
    public ResponseEntity<ScheduleAppointmentDTO> updateScheduleAppointment(
            @PathVariable long id, @RequestBody ScheduleAppointmentDTO scheduleAppointmentDTO) {
        ScheduleAppointment scheduleAppointment =
                scheduleAppointmentService.updateScheduleAppointment(id, scheduleAppointmentDTO);
        return ResponseEntity.ok(scheduleAppointmentMapper.mapEntityToDTO(scheduleAppointment));
    }

    @PutMapping("/{id}/status")
    @ApiOperation("Update schedule status")
    public ResponseEntity<ScheduleAppointmentDTO> updateScheduleAppointmentStatus(
            @PathVariable long id, @RequestParam int status) {
        ScheduleAppointment scheduleAppointment =
                scheduleAppointmentService.updateScheduleAppointmentStatus(id, status);
        return ResponseEntity.ok(scheduleAppointmentMapper.mapEntityToDTO(scheduleAppointment));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete schedule")
    public ResponseEntity<String> deleteScheduleAppointment(@PathVariable long id) {
        scheduleAppointmentService.deleteScheduleAppointment(id);
        return ResponseEntity.ok("Appointment deleted with success.");
    }
}
