package cow.starter.bovine.models;

import cow.starter.field.models.Field;
import cow.starter.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BovineFullInfoDTO {
    private long idBovine;
    private String idContract;
    private User user;
    private Field field;
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
    private String age;
    private String imageCID;

    public BovineFullInfoDTO(long idBovine, String idContract, User user, Field field, long serialNumber,
                             String birthDate, Double weight, Double height, String breed, String color, Boolean active,
                             String observation, long idBovineParent1, long idBovineParent2,
                             boolean gender, String imageCID) {
        this.idBovine = idBovine;
        this.idContract = idContract;
        this.user = user;
        this.field = field;
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
}
