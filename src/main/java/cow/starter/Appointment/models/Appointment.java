package cow.starter.Appointment.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import cow.starter.Bovine.models.Bovine;
import cow.starter.User.models.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "COW_Appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAppointment;

    @Column(nullable = false)
    private String idContract;

    private long idAppointmentRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bovine")
    @JsonBackReference
    private Bovine bovine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private Date appointmentDate;

    @Column(nullable = false, length = 50)
    private String appointmentType;

    @Column(nullable = false)
    private Double cost;

    private String observation;

    @Column(nullable = false)
    private int appointmentStatus;

    public Appointment(String idContract, long idAppointmentRequest, Bovine bovine, User user, Date appointmentDate,
                       String appointmentType, Double cost, String observation, int status) {
        this.idContract = idContract;
        this.idAppointmentRequest = idAppointmentRequest;
        this.bovine = bovine;
        this.user = user;
        this.appointmentDate = appointmentDate;
        this.appointmentType = appointmentType;
        this.cost = cost;
        this.observation = observation;
        this.appointmentStatus = status;
    }
}
