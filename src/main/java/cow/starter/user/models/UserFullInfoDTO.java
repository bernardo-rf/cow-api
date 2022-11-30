package cow.starter.user.models;

import java.util.Date;

public class UserFullInfoDTO {
    private long idUser;
    private String idContract;
    private String idWallet;
    private long idUserType;
    private String type;
    private String name;
    private Date birthDate;
    private String email;
    private String password;
    private Boolean active;
    private Double balance;
    private String fullName;
    private String imageCID;

    public UserFullInfoDTO() { super(); }

    public UserFullInfoDTO(long idUser, String idContract, String idWallet, long idUserType, String type, String name,
                           Date birthDate, String email, String password, Boolean active, Double balance,
                           String fullName, String imageCID ) {
        this.idUser = idUser;
        this.idContract = idContract;
        this.idWallet = idWallet;
        this.idUserType = idUserType;
        this.type = type;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.active = active;
        this.balance = balance;
        this.fullName = fullName;
        this.imageCID = imageCID;
    }

    public long getIdUser() { return idUser; }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getIdContract() {
        return idContract;
    }

    public void setIdContract(String idContract) {
        this.idContract = idContract;
    }

    public String getIdWallet() {
        return idWallet;
    }

    public void setIdWallet(String idWallet) {
        this.idWallet = idWallet;
    }

    public long getIdUserType() {
        return idUserType;
    }

    public void setIdUserType(long idUserType) {
        this.idUserType = idUserType;
    }

    public String getUserType() { return type; }

    public void setUserType(String type) { this.type = type; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImageCID() {
        return imageCID;
    }

    public void setImageCID(String imageCID) {
        this.imageCID = imageCID;
    }
}
