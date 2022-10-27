package cow.starter.Appointment.models;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="COW_Appointment")
public class Appointment implements Serializable {
    /**
     * @param idAppointment
     * @param idContract
     * @param idBovine
     * @param idVeterinary
     * @param appointmentDate
     * @param appointmentType
     * @param cost
     * @param observation
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "1", name = "idAppointment", required = true)
    private long idAppointment;

    @Column(nullable = false)
    @ApiModelProperty(example = "0.0.48207803", name = "idContract", required = true)
    private String idContract;

    @ApiModelProperty(example = "1", name = "idAppointmentRequest", required = true)
    private long idAppointmentRequest;

    @Column(nullable = false)
    @ApiModelProperty(example = "1", name = "idBovine", required = true)
    private long idBovine;

    @Column(nullable = false)
    @ApiModelProperty(example = "1", name = "idUser", required = true)
    private long idUser;

    @Column(nullable = false)
    @ApiModelProperty(example = "2022-01-01", name = "appointmentDate", required = true)
    private Date appointmentDate;

    @Column(nullable = false, length = 50)
    @ApiModelProperty(example = "Regular appointment", name = "type", required = true)
    private String appointmentType;

    @Column(nullable = false)
    @ApiModelProperty(example = "5.00", name = "cost", required = true)
    private Double cost;

    @ApiModelProperty(example = "Appointment observation", name = "observation")
    private String observation;

    @Column(nullable = false)
    @ApiModelProperty(example = "1", name = "status", required = true)
    private int appointmentStatus;


    public Appointment() {
        super();
    }

    public Appointment(String idContract, long idAppointmentRequest, long idBovine, long idUser, Date appointmentDate, String appointmentType, Double cost,
                       String observation, int status ) {
        this.idContract = idContract;
        this.idAppointmentRequest = idAppointmentRequest;
        this.idBovine = idBovine;
        this.idUser = idUser;
        this.appointmentDate = appointmentDate;
        this.appointmentType = appointmentType;
        this.cost = cost;
        this.observation = observation;
        this.appointmentStatus = status;
    }

    public long getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(long idAppointment) {
        this.idAppointment = idAppointment;
    }

    public long getIdAppointmentRequest() { return idAppointmentRequest; }

    public void setIdAppointmentRequest(long idAppointmentRequest) {
        this.idAppointmentRequest = idAppointmentRequest;
    }

    public String getIdContract() {
        return idContract;
    }

    public void setIdContract(String idContract) {
        this.idContract = idContract;
    }

    public long getIdBovine() {
        return idBovine;
    }

    public void setIdBovine(long idBovine) {
        this.idBovine = idBovine;
    }

    public long getIdUser() { return idUser; }

    public void setIdUser(long idUser) { this.idUser = idUser; }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public int getStatus() { return appointmentStatus; }

    public void setStatus(int status) { this.appointmentStatus = status; }
}
