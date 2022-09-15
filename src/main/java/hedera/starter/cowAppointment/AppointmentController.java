package hedera.starter.cowAppointment;

import com.hedera.hashgraph.sdk.ContractId;
import hedera.starter.cowAppointment.models.Appointment;
import hedera.starter.cowAppointment.models.AppointmentRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@Api("Handles management of COW Appointment")
@RequestMapping(path = "api/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public AppointmentController() {
        appointmentService = new AppointmentService();
    }

    @GetMapping("/")
    @ApiOperation("Get all appointments")
    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/{idAppointment}")
    @ApiOperation("Get appointment by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idAppointment", required = true, type = "long", example = "1",
                    value = "appointment identifier.")
    })
    public Optional<Appointment> getAppointments(@PathVariable long idAppointment) {
        return appointmentRepository.findById(idAppointment);
    }

    @PostMapping("/")
    @ApiOperation("Create a appointment")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "idBovine",type = "int", example = "1",
                    value = "define the identifier of the bovine."),
            @ApiImplicitParam( name = "idVeterinary",type = "int", example = "1",
                    value = "define the identifier of the bovine."),
            @ApiImplicitParam( name = "appointmentDate", type = "date", example = "2022/01/01",
                    value = "define the date of the appointment."),
            @ApiImplicitParam( name = "appointmentType", type = "string", example = "Regular Appointment",
                    value = "define the type of the appointment."),
            @ApiImplicitParam( name = "cost", type = "double", example = "5.00",
                    value = "define the cost of the appointment."),
            @ApiImplicitParam( name = "observation", type = "string", example = "Observation about the appointment",
                    value = "define the observation of the appointment.")
    })
    public Appointment createField( @RequestParam(defaultValue = "1") int idBovine,
                                    @RequestParam(defaultValue = "1") int idVeterinary,
                                    @RequestParam(defaultValue = "2022-01-01") Date appointmentDate,
                                    @RequestParam(defaultValue = "Regular Appointment") String appointmentType,
                                    @RequestParam(defaultValue = "5.00") Double cost,
                                    @RequestParam(defaultValue = "Appointment observation") String observation) {
        ContractId idContract = appointmentService.createAppointment(idBovine, idVeterinary, appointmentDate,
                appointmentType, cost, observation);
        Appointment newAppointment = new Appointment(idContract.toString(), idBovine, idVeterinary, appointmentDate,
                appointmentType, cost, observation);
        return appointmentRepository.save(newAppointment);
    }

    @PutMapping("/{idAppointment}")
    @ApiOperation("Update a appointment")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "idBovine",type = "int", example = "1",
                    value = "define the identifier of the bovine."),
            @ApiImplicitParam( name = "idVeterinary",type = "int", example = "1",
                    value = "define the identifier of the bovine."),
            @ApiImplicitParam( name = "appointmentDate", type = "date", example = "2022-01-01",
                    value = "define the date of the appointment."),
            @ApiImplicitParam( name = "appointmentType", type = "string", example = "Regular Appointment",
                    value = "define the type of the appointment."),
            @ApiImplicitParam( name = "cost", type = "double", example = "5.00",
                    value = "define the cost of the appointment."),
            @ApiImplicitParam( name = "observation", type = "string", example = "Observation about the appointment",
                    value = "define the observation of the appointment.")
    })
    public Appointment updateAppointment( @PathVariable long idAppointment,
                              @RequestParam(defaultValue = "1") int idBovine,
                              @RequestParam(defaultValue = "1") int idVeterinary,
                              @RequestParam(defaultValue = "2022-01-01") Date appointmentDate,
                              @RequestParam(defaultValue = "Regular Appointment") String appointmentType,
                              @RequestParam(defaultValue = "5.00") Double cost,
                              @RequestParam(defaultValue = "Appointment observation") String observation) {
        Appointment oldAppointment = appointmentRepository.findById(idAppointment).get();
        appointmentService.updateAppointment(oldAppointment.getIdContract(), idBovine, idVeterinary, appointmentDate,
                appointmentType, cost, observation);
        oldAppointment.setIdBovine(idBovine);
        oldAppointment.setIdVeterinary(idVeterinary);
        oldAppointment.setAppointmentDate(appointmentDate);
        oldAppointment.setAppointmentType(appointmentType);
        oldAppointment.setCost(cost);
        oldAppointment.setObservation(observation);

        return appointmentRepository.save(oldAppointment);
    }

    @DeleteMapping("/{idAppointment}")
    @ApiOperation("Delete a appointment")
    public void deleteAppointment( @PathVariable long idAppointment) {
        Appointment oldAppointment = appointmentRepository.findById(idAppointment).get();
        appointmentService.deleteAppointment(oldAppointment.getIdContract());
        appointmentRepository.deleteById(idAppointment);
    }
}
