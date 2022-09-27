package cow.starter.Field.models;

public class FieldFullInfoDTO {
    private long idField;
    private String idOwner;
    private String idContract;
    private String fieldDescription;
    private Double latitude;
    private Double longitude;
    private String address;
    private int limit;
    private Boolean active;
    private String observation;
    private int currentOccupation;
    private int currentOccupationPercentage;

    public FieldFullInfoDTO() { }

    public FieldFullInfoDTO(long idField, String idOwner, String idContract, String fieldDescription, String address,
                            Double latitude, Double longitude, int limit, int currentOccupation, Boolean active,
                            String observation, int currentOccupationPercentage) {
        this.idField = idField;
        this.idOwner = idOwner;
        this.idContract = idContract;
        this.fieldDescription = fieldDescription;
        this.address = address;
        this.limit = limit;
        this.currentOccupation = currentOccupation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
        this.observation = observation;
        this.currentOccupationPercentage = currentOccupationPercentage;
    }

    public long getIdField() {
        return idField;
    }

    public void setIdField(long idField) {
        this.idField = idField;
    }

    public String getIdContract() {
        return idContract;
    }

    public void setIdContract(String idContract) {
        this.idContract = idContract;
    }

    public String getIdOwner() { return idOwner; }

    public void setIdOwner(String idOwner) { this.idOwner = idOwner; }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCurrentOccupation() {
        return currentOccupation;
    }

    public void setCurrentOccupation(int currentOccupation) {
        this.currentOccupation = currentOccupation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public int getCurrentOccupationPercentage() {
        return currentOccupationPercentage;
    }

    public void setCurrentOccupationPercentage(int currentOccupationPercentage) {
        this.currentOccupationPercentage = currentOccupationPercentage;
    }
}
