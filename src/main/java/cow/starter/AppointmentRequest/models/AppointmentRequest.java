package cow.starter.AppointmentRequest.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import cow.starter.Bovine.models.Bovine;
import cow.starter.User.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="COW_AppointmentRequest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAppointmentRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_request")
    @JsonBackReference
    private User userRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bovine")
    @JsonBackReference
    private Bovine bovine;

    @Column(nullable = false)
    private Date appointmentDate;

    @Column(nullable = false, length = 50)
    private String motive;

    @Column(nullable = false)
    private int appointmentRequestStatus;

    public AppointmentRequest(User user, User userRequest, Bovine bovine, Date appointmentDate, String motive,
                              int status ) {
        this.user = user;
        this.userRequest = userRequest;
        this.bovine = bovine;
        this.appointmentDate = appointmentDate;
        this.motive = motive;
        this.appointmentRequestStatus = status;
    }
}
