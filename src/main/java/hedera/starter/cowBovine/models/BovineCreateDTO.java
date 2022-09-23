package hedera.starter.cowBovine.models;

import java.util.Date;
import java.util.List;

public class BovineCreateDTO {
    private String idOwner;
    private long idField;
    private long serialNumber;
    private Date birthDate;
    private Double weight;
    private Double height;
    private String breed;
    private String color;
    private Boolean active;
    private String observation;
    private long idBovineParent1;
    private long idBovineParent2;
    private boolean gender;

    public BovineCreateDTO() { }

    public BovineCreateDTO(String idOwner, long idField, long serialNumber, Date birthDate, Double weight, Double height, String breed, String color, Boolean active, String observation, long idBovineParent1, long idBovineParent2, boolean gender) {
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

    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
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

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
