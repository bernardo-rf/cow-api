package cow.starter.Appointment.models;

public class AppointmentFullInfoDTO {
    private long idAppointment;
    private String idContract;
    private long idAppointmentRequest;
    private long idBovine;
    private long idUser;
    private String appointmentDate;
    private String appointmentType;
    private Double cost;
    private String observation;
    private long bovineSerialNumber;
    private int status;

    public AppointmentFullInfoDTO(long idAppointment, String idContract, long idAppointmentRequest, long idBovine,
                                  long idUser, String appointmentDate, String appointmentType, Double cost,
                                  String observation, long bovineSerialNumber, int status) {
        this.idAppointment = idAppointment;
        this.idContract = idContract;
        this.idAppointmentRequest = idAppointmentRequest;
        this.idBovine = idBovine;
        this.idUser = idUser;
        this.appointmentDate = appointmentDate;
        this.appointmentType = appointmentType;
        this.cost = cost;
        this.observation = observation;
        this.bovineSerialNumber = bovineSerialNumber;
        this.status = status;
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

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
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

    public long getBovineSerialNumber() {
        return bovineSerialNumber;
    }

    public void setBovineSerialNumber(long bovineSerialNumber) {
        this.bovineSerialNumber = bovineSerialNumber;
    }

    public long getIdAppointmentRequest() { return idAppointmentRequest; }

    public void setIdAppointmentRequest(long idAppointmentRequest) {
        this.idAppointmentRequest = idAppointmentRequest;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
