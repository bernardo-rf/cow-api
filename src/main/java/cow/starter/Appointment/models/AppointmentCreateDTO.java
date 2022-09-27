package cow.starter.Appointment.models;

import java.util.Date;

public class AppointmentCreateDTO {
    private long idBovine;
    private long idUser;
    private Date appointmentDate;
    private String appointmentType;
    private Double cost;
    private String observation;

    public AppointmentCreateDTO() { }

    public AppointmentCreateDTO(long idBovine, long idUser, Date appointmentDate, String appointmentType, Double cost,
                                String observation) {
        this.idBovine = idBovine;
        this.idUser = idUser;
        this.appointmentDate = appointmentDate;
        this.appointmentType = appointmentType;
        this.cost = cost;
        this.observation = observation;
    }

    public long getIdBovine() { return idBovine; }

    public void setIdBovine(long idBovine) {
        this.idBovine = idBovine;
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
