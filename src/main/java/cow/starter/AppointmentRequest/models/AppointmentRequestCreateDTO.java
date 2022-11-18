package cow.starter.AppointmentRequest.models;

import cow.starter.Bovine.models.Bovine;
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
public class AppointmentRequestCreateDTO {
    private long idUser;
    private long idUserRequest;
    private Date appointmentDate;
    private String motive;
    private int status;
    private ArrayList<Integer> bovineIds;
}
