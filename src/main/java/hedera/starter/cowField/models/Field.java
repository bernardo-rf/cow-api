package hedera.starter.cowField.models;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="COW_Field")
public class Field implements Serializable {
    /**
     * @param idField
     * @param idContract
     * @param idOwner
     * @param fieldDescription
     * @param address
     * @param limit
     * @param latitude
     * @param longitude
     * @param active
     * @param observation
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDField")
    @ApiModelProperty(example = "1", name = "idField", required = true)
    private long idField;

    @Column(name = "IDContract")
    @ApiModelProperty(example = "0.0.48207803", name = "idContract")
    private String idContract;

    @Column(name = "IDOwner")
    @ApiModelProperty(example = "1", name = "idOwner")
    private String idOwner;

    @Column(name = "Description")
    @ApiModelProperty(example = "West Farm", name = "fieldDescription", required = true)
    private String fieldDescription;

    @Column(name = "Address")
    @ApiModelProperty(example = "537-3270 1109 19th St. Gil, Nebraska(NE)", name = "address", required = true)
    private String address;

    @Column(name = "Limit")
    @ApiModelProperty(example = "100", name = "limit", required = true)
    private int limit;

    @Column(name = "Latitude")
    @ApiModelProperty(example = "37.89143", name = "latitude", required = true)
    private Double latitude;

    @Column(name = "Longitude")
    @ApiModelProperty(example = "66.31465", name = "longitude", required = true)
    private Double longitude;

    @Column(name = "Active")
    @ApiModelProperty(example = "1", name = "active", required = true)
    private Boolean active;

    @Column(name = "Observation")
    @ApiModelProperty(example = "Field with too much trees, short space.", name = "observation", required = false)
    private String observation;

    public Field() {
        super();
    }

    public Field(String idContract, String idOwner, String fieldDescription, String address, int limit, Double latitude,
                 Double longitude, Boolean active, String observation) {
        this.idContract = idContract;
        this.idOwner = idOwner;
        this.fieldDescription = fieldDescription;
        this.address = address;
        this.limit = limit;
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

    public String getIdContract() { return idContract; }

    public void setIdContract(String idContract) { this.idContract = idContract; }

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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
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