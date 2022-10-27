package cow.starter.AppointmentRequest.models;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="COW_AppointmentRequest")
public class AppointmentRequest implements Serializable {
    /**
     * @param idAppointmentRequest
     * @param idUser
     * @param idBovine
     * @param appointmentDate
     * @param motive
     * @param status
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "1", name = "idAppointmentRequest", required = true)
    private long idAppointmentRequest;

    @Column(nullable = false)
    @ApiModelProperty(example = "1", name = "idUser", required = true)
    private long idUser;

    @Column(nullable = false )
    @ApiModelProperty(example = "1", name = "idUserRequest", required = true)
    private long idUserRequest;

    @Column(nullable = false)
    @ApiModelProperty(example = "1", name = "idBovine", required = true)
    private long idBovine;

    @Column(nullable = false)
    @ApiModelProperty(example = "2022-01-01", name = "appointmentDate", required = true)
    private Date appointmentDate;

    @Column(nullable = false, length = 50)
    @ApiModelProperty(example = "Regular Appoitment", name = "motive", required = true)
    private String motive;

    @Column(nullable = false)
    @ApiModelProperty(example = "true", name = "status", required = true)
    private int appointmentRequestStatus;

    public AppointmentRequest() { super(); }

    public AppointmentRequest(long idUser, long idUserRequest, long idBovine, Date appointmentDate, String motive,
                              int status ) {
        this.idUser = idUser;
        this.idUserRequest = idUserRequest;
        this.idBovine = idBovine;
        this.appointmentDate = appointmentDate;
        this.motive = motive;
        this.appointmentRequestStatus = status;
    }

    public long getIdAppointmentRequest() {
        return idAppointmentRequest;
    }

    public void setIdAppointmentRequest(long idAppointmentRequest) {
        this.idAppointmentRequest = idAppointmentRequest;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdUserRequest() {
        return idUserRequest;
    }

    public void setIdUserRequest(long idUserRequest) {
        this.idUserRequest = idUserRequest;
    }

    public long getIdBovine() {
        return idBovine;
    }

    public void setIdBovine(long idBovine) {
        this.idBovine = idBovine;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public int getStatus() {
        return appointmentRequestStatus;
    }

    public void setStatus(int status) {
        this.appointmentRequestStatus = status;
    }
}
