package hedera.starter.cowBovine.models;

import io.swagger.annotations.ApiModelProperty;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="COW_Bovine")
public class Bovine implements Serializable {

    /**
     * @param idBovine
     * @param idContract
     * @param idField
     * @param serialNumber
     * @param birthDate
     * @param weight
     * @param height
     * @param breed
     * @param color
     * @param active
     * @param observation
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDBovine")
    @ApiModelProperty(example = "1", name = "idBovine", required = true)
    private long idBovine;

    @Column(name = "IDContract")
    @ApiModelProperty(example = "1", name = "idContract")
    private String idContract;

    @Column(name = "IDWallet")
    @ApiModelProperty(example = "1", name = "idWallet")
    private String idOwner;

    @Column(name = "IDField")
    @ApiModelProperty(example = "1", name = "idField")
    private long idField;

    @Column(name = "SerialNumber")
    @ApiModelProperty(example = "12345", name = "serialNumber", required = true)
    private Integer serialNumber;

    @Column(name = "BirthDate")
    @ApiModelProperty(example = "2022-01-01", name = "birthDate", required = true)
    private Date birthDate;

    @Column(name = "Weight")
    @ApiModelProperty(example = "102.20", name = "weight", required = true)
    private Double weight;

    @Column(name = "Height")
    @ApiModelProperty(example = "1.00", name = "height", required = true)
    private Double height;

    @Column(name = "Breed")
    @ApiModelProperty(example = "Angus", name = "breed", required = true)
    private String breed;

    @Column(name = "Color")
    @ApiModelProperty(example = "White", name = "color", required = true)
    private String color;

    @Column(name = "Active")
    @ApiModelProperty(example = "1", name = "active", required = true)
    private Boolean active;

    @Column(name = "Observation")
    @ApiModelProperty(example = "Bovine observation.", name = "observation")
    private String observation;

    @Column(name = "IDBovineParent1")
    @Nullable
    @ApiModelProperty(example = "Bovine identifier parent 1.", name = "idBovineParent1")
    private long idBovineParent1;

    @Column(name = "IDBovineParent2")
    @Nullable
    @ApiModelProperty(example = "Bovine identifier parent 2.", name = "idBovineParent2")
    private long idBovineParent2;

    @Column(name = "Gender")
    @ApiModelProperty(example = "Bovine identifier gender.", name = "gender")
    private boolean gender;

    public Bovine() { super(); }

    public Bovine(String idContract, String idOwner, long idField, Date birthDate, Double weight, Double height, String breed,
                  String color, Boolean active, String observation, long idBovineParent1, long idBovineParent2,
                  boolean gender ) {
        this.idContract = idContract;
        this.idOwner = idOwner;
        this.idField = idField;
        this.birthDate = birthDate;
        this.weight = weight;
        this.height = height;
        this.breed = breed;
        this.color = color;
        this.active = active;
        this.observation = observation;
        this.idBovineParent1 = idBovineParent1;
        this.idBovineParent2 = idBovineParent2;
        this.gender = gender;
    }

    public long getIdBovine() {
        return idBovine;
    }

    public void setIdBovine(long idBovine) {
        this.idBovine = idBovine;
    }

    public String getIdContract() {
        return idContract;
    }

    public void setIdContract(String idContract) {
        this.idContract = idContract;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public long getIdField() {
        return idField;
    }

    public void setIdField(long idField) {
        this.idField = idField;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public long getIdBovineParent1() {
        return idBovineParent1;
    }

    public void setIdBovineParent1(long idBovineParent1) {
        this.idBovineParent1 = idBovineParent1;
    }

    public long getIdBovineParent2() {
        return idBovineParent2;
    }

    public void setIdBovineParent2(long idBovineParent2) {
        this.idBovineParent2 = idBovineParent2;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
