package cow.starter.AppointmentRequest.models;

import cow.starter.Bovine.models.Bovine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentRequestCreateDTO {
    private long idUser;
    private long idUserRequest;
    private Date appointmentDate;
    private String motive;
    private int status;
    private List<Bovine> bovines;

    public AppointmentRequestCreateDTO( ) { }

    public AppointmentRequestCreateDTO(long idUser, long idUserRequest, Date appointmentDate,
                                       String motive, int status) {
        this.idUser = idUser;
        this.idUserRequest = idUserRequest;
        this.bovines = new ArrayList<>();
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
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Bovine> getBovines() {
        return bovines;
    }

    public void setBovines(List<Bovine> bovines) {
        this.bovines = bovines;
    }
}
