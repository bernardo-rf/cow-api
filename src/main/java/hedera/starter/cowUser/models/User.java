package hedera.starter.cowUser.models;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="COW_User")
public class User implements Serializable {
    /**
     * @param idUser
     * @param idContract
     * @param idWallet
     * @param idUserType
     * @param name
     * @param birthDate
     * @param email
     * @param password
     * @param active
     * @param balance
     */

    @Id
    @Column(name = "IDUser")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "1", name = "idUser", required = true)
    private long idUser;

    @Column(name = "IDContract")
    @ApiModelProperty(example = "1", name = "idContract")
    private String idContract;

    @Column(name = "IDWallet")
    @ApiModelProperty(example = "1", name = "idWallet", required = true)
    private String idWallet;

    @Column(name = "IDUsertype")
    @ApiModelProperty(example = "1", name = "idUserType", required = true)
    private int idUserType;

    @Column(name = "Name")
    @ApiModelProperty(example = "1", name = "name", required = true)
    private String name;

    @Column(name = "birthdate")
    @ApiModelProperty(example = "2022-01-01", name = "birthDate")
    private Date birthDate;

    @Column(name = "Email")
    @ApiModelProperty(example = "Regular appointment", name = "email", required = true)
    private String email;

    @Column(name = "Password")
    @ApiModelProperty(example = "Regular appointment", name = "password", required = true)
    private String password;

    @Column(name = "Active")
    @ApiModelProperty(example = "1", name = "active", required = true)
    private Boolean active;

    @Column(name = "Balance")
    @ApiModelProperty(example = "1090.50", name="balance", required = true)
    private double balance;

    public User() { super(); }

    public User(String idContract, String idWallet, int idUserType, String name, Date birthDate, String email,
                String password, Boolean active, Double balance) {
        this.idContract = idContract;
        this.idWallet = idWallet;
        this.idUserType = idUserType;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.active = active;
        this.balance = balance;
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

    public int getIdUserType() {
        return idUserType;
    }

    public void setIdUserType(int idUserType) {
        this.idUserType = idUserType;
    }

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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
