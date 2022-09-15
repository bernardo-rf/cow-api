package hedera.starter.cowField.models;

public class FieldFullInfoDTO {
    private long idField;
    private Boolean active;
    private String fieldDescription;
    private String idContract;
    private Double latitude;
    private Double longitude;
    private String address;
    private int limit;
    private String observation;
    private Integer currentOccupation;

    public FieldFullInfoDTO() { }

    public FieldFullInfoDTO(long idField, String idContract, String fieldDescription, String address, int limit,
                            Integer currentOccupation, Double latitude, Double longitude, Boolean active, String observation) {
        this.idField = idField;
        this.idContract = idContract;
        this.fieldDescription = fieldDescription;
        this.address = address;
        this.limit = limit;
        this.currentOccupation = currentOccupation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
        this.observation = observation;
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

    public Integer getCurrentOccupation() {
        return currentOccupation;
    }

    public void setCurrentOccupation(Integer currentOccupation) {
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
}
