package cow.starter.AppointmentRequest.models;

public class AppointmentRequestFullInfoDTO {
    private long idAppointmentRequest;
    private long idUser;
    private long idUserRequest;
    private long idBovine;
    private String appointmentDate;
    private String motive;
    private int status;
    private String userName;
    private String userRequestName;
    private long bovineSerialNumber;

    public AppointmentRequestFullInfoDTO(){}

    public AppointmentRequestFullInfoDTO(long idAppointmentRequest, long idUser, long idUserRequest, long idBovine,
                                         String appointmentDate, String motive, int status, String userName,
                                         String userRequestName, long bovineSerialNumber) {
        this.idAppointmentRequest = idAppointmentRequest;
        this.idUser = idUser;
        this.idUserRequest = idUserRequest;
        this.idBovine = idBovine;
        this.appointmentDate = appointmentDate;
        this.motive = motive;
        this.status = status;
        this.userName = userName;
        this.userRequestName = userRequestName;
        this.bovineSerialNumber = bovineSerialNumber;
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

    public int getStatus() { return status; }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRequestName() {
        return userRequestName;
    }

    public void setUserRequestName(String userRequestName) {
        this.userRequestName = userRequestName;
    }

    public long getBovineSerialNumber() {
        return bovineSerialNumber;
    }

    public void setBovineSerialNumber(long bovineSerialNumber) {
        this.bovineSerialNumber = bovineSerialNumber;
    }
}
