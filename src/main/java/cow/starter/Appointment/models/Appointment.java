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
    @Column(name = "IDAppointment")
    @ApiModelProperty(example = "1", name = "idAppointment", required = true)
    private long idAppointment;

    @Column(name = "IDContract", nullable = false)
    @ApiModelProperty(example = "0.0.48207803", name = "idContract")
    private String idContract;

    @Column(name = "IDBovine", nullable = false)
    @ApiModelProperty(example = "1", name = "idBovine", required = true)
    private long idBovine;

    @Column(name = "IDUser", nullable = false)
    @ApiModelProperty(example = "1", name = "idUser")
    private long idUser;

    @Column(name = "appointmentdate", nullable = false)
    @ApiModelProperty(example = "2022-01-01", name = "appointmentDate")
    private Date appointmentDate;

    @Column(name = "appointmenttype", nullable = false, length = 50)
    @ApiModelProperty(example = "Regular appointment", name = "type")
    private String appointmentType;

    @Column(name = "Cost", nullable = false)
    @ApiModelProperty(example = "5.00 hbar", name = "cost")
    private Double cost;

    @Column(name = "Observation")
    @ApiModelProperty(example = "Appointment observation", name = "observation")
    private String observation;

    public Appointment() {
        super();
    }

    public Appointment(String idContract, long idBovine, long idUser, Date appointmentDate, String appointmentType, Double cost,
                       String observation  ) {
        this.idContract = idContract;
        this.idBovine = idBovine;
        this.idUser = idUser;
        this.appointmentDate = appointmentDate;
        this.appointmentType = appointmentType;
        this.cost = cost;
        this.observation = observation;
    }

    public long getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(long idAppointment) {
        this.idAppointment = idAppointment;
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
}
