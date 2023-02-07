package cow.starter.Appointment.models;

import cow.starter.Bovine.models.Bovine;
import java.util.Date;
import java.util.List;

public class AppointmentCreateDTO {
    private long idAppointmentRequest;
    private long idUser;
    private Date appointmentDate;
    private String appointmentType;
    private Double cost;
    private String observation;
    private int status;
    private List<Bovine> bovines;

    public AppointmentCreateDTO() { }

    public AppointmentCreateDTO(long idAppointmentRequest, long idUser, Date appointmentDate, String appointmentType,
                                Double cost, String observation, int status, List<Bovine> bovines) {
        this.idAppointmentRequest = idAppointmentRequest;
        this.bovines = bovines;
        this.idUser = idUser;
        this.appointmentDate = appointmentDate;
        this.appointmentType = appointmentType;
        this.cost = cost;
        this.observation = observation;
        this.status = status;
    }

    public long getIdAppointmentRequest() { return idAppointmentRequest; }

    public void setIdAppointmentRequest(long idAppointmentRequest) {
        this.idAppointmentRequest = idAppointmentRequest;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentType() { return appointmentType; }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Bovine> getBovines() { return bovines; }

    public void setBovines(List<Bovine> bovines) { this.bovines = bovines; }
}
