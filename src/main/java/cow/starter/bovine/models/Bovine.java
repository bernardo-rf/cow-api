package cow.starter.Bovine.models;

import io.swagger.annotations.ApiModelProperty;
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
     * @param idBovineParent1
     * @param idBovineParent2
     * @param gender
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBovine;

    @Column(nullable = false)
    private String idContract;

    @Column(nullable = false)
    private String idOwner;

    @Column(nullable = false)
    private long idField;

    @Column(nullable = false)
    private long serialNumber;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Boolean active;

    private String observation;

    private long idBovineParent1;

    private long idBovineParent2;

    @Column(nullable = false)
    private boolean gender;

    private String imageCID;

    public Bovine() { super(); }

    public Bovine(String idContract, String idOwner, long idField, long serialNumber,  Date birthDate, Double weight,
                  Double height, String breed, String color, Boolean active, String observation, long idBovineParent1,
                  long idBovineParent2, boolean gender, String imageCID ) {
        this.idContract = idContract;
        this.idOwner = idOwner;
        this.idField = idField;
        this.serialNumber = serialNumber;
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
        this.imageCID = imageCID;
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

    public long getSerialNumber() { return serialNumber; }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getBirthDate() { return birthDate; }

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

    public String getImageCID() {
        return imageCID;
    }

    public void setImageCID(String imageCID) {
        this.imageCID = imageCID;
    }
}
