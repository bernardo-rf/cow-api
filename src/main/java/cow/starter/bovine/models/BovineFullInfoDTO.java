package cow.starter.Bovine.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class BovineFullInfoDTO {
    private long idBovine;
    private String idContract;
    private String idOwner;
    private long idField;
    private long serialNumber;
    private String birthDate;
    private Double weight;
    private Double height;
    private String breed;
    private String color;
    private Boolean active;
    private String observation;
    private long idBovineParent1;
    private long idBovineParent2;
    private boolean gender;
    private String address;
    private String age;
    private String imageCID;

    public BovineFullInfoDTO(){};

    public BovineFullInfoDTO(long idBovine, String idContract, String idOwner, long idField, long serialNumber,
                             String birthDate, Double weight, Double height, String breed, String color, Boolean active,
                             String observation, long idBovineParent1, long idBovineParent2,
                             boolean gender, String address, String imageCID) {
        this.idBovine = idBovine;
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
        this.address = address;
        this.imageCID = imageCID;

        LocalDate birthDateTST = LocalDate.parse(birthDate.split(" ")[0]);
        int age = Period.between(birthDateTST, LocalDate.now()).getYears();
        this.age = age + " years";
        if (age <= 0) {
            age = Period.between(birthDateTST, LocalDate.now()).getMonths();
            this.age = age + " months";
            if (age <= 0) {
                age = Period.between(birthDateTST, LocalDate.now()).getDays();
                this.age = age + " days";
            }
        }
        if(age == 1){
            this.age = this.age.substring(0, this.age.length()-1);
        }
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

    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
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

    public void setColor(String color) { this.color = color; }

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

    public String getAddress() { return address; }

    public void setAddress(String address) {
        this.address = address;
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

    public boolean isGender() { return gender; }

    public void setGender(boolean gender) { this.gender = gender; }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImageCID() {
        return imageCID;
    }

    public void setImageCID(String imageCID) {
        this.imageCID = imageCID;
    }
}
