package cow.starter.Field.models;

import cow.starter.Bovine.models.BovineDTO;
import java.util.List;

public class FieldCreateDTO {
    private String idOwner;
    private String fieldDescription;
    private String address;
    private Double latitude;
    private Double longitude;
    private int limit;
    private Boolean active;
    private String observation;
    private List<BovineDTO> bovines;

    public FieldCreateDTO() { }

    public FieldCreateDTO(String idOwner, String fieldDescription, String address, Double latitude, int limit,
                          Double longitude, Boolean active,  String observation, List<BovineDTO> bovines) {
        this.idOwner = idOwner;
        this.fieldDescription = fieldDescription;
        this.address = address;
        this.limit = limit;
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
        this.observation = observation;
        this.bovines = bovines;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
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

    public List<BovineDTO> getBovines() {
        return bovines;
    }

    public void setBovines(List<BovineDTO> bovines) {
        this.bovines = bovines;
    }
}
