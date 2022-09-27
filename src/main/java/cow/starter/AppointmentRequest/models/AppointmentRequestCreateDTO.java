package cow.starter.AppointmentRequest.models;

import java.util.Date;

public class AppointmentRequestCreateDTO {
    private long idUser;
    private long idUserRequest;
    private long idBovine;
    private Date appointmentDate;
    private String motive;
    private Boolean status;

    public AppointmentRequestCreateDTO( ) { }

    public AppointmentRequestCreateDTO(long idUser, long idUserRequest, long idBovine, Date appointmentDate, String motive, Boolean status) {
        this.idUser = idUser;
        this.idUserRequest = idUserRequest;
        this.idBovine = idBovine;
        this.appointmentDate = appointmentDate;
        this.motive = motive;
        this.status = status;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
