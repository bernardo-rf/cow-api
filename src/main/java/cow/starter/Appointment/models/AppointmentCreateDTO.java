package cow.starter.Appointment.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateDTO {
    private long idAppointmentRequest;
    private long idUser;
    private Date appointmentDate;
    private String appointmentType;
    private Double cost;
    private String observation;
    private int status;
    private ArrayList<Integer> bovineIds;
}
