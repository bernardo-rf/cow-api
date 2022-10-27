package cow.starter.AppointmentRequest.models;

public class AppointmentRequestDTO {
    private long idAppointmentRequest;
    private long idUser;
    private long idUserRequest;
    private long idBovine;
    private String appointmentDate;
    private String motive;
    private int status;

    public AppointmentRequestDTO( ) { }

    public AppointmentRequestDTO(long idAppointmentRequest, long idUser, long idUserRequest, long idBovine,
                                 String appointmentDate, String motive, int status) {
        this.idAppointmentRequest = idAppointmentRequest;
        this.idUser = idUser;
        this.idUserRequest = idUserRequest;
        this.idBovine = idBovine;
        this.appointmentDate = appointmentDate;
        this.motive = motive;
        this.status = status;
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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
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
}
