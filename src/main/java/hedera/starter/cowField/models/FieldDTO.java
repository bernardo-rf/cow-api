package hedera.starter.cowField.models;

public class FieldDTO {
    private long idField;
    private Boolean active;
    private String fieldDescription;
    private String idContract;
    private Double latitude;
    private Double longitude;
    private String address;
    private int limit;
    private String observation;

    public FieldDTO(){}

    public FieldDTO(long idField, Boolean active, String fieldDescription, String idContract, Double latitude, Double longitude, String address, int limit, String observation) {
        this.idField = idField;
        this.active = active;
        this.fieldDescription = fieldDescription;
        this.idContract = idContract;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.limit = limit;
        this.observation = observation;
    }

    public long getIdField() {
        return idField;
    }

    public void setIdField(long idField) {
        this.idField = idField;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public String getIdContract() {
        return idContract;
    }

    public void setIdContract(String idContract) {
        this.idContract = idContract;
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

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
