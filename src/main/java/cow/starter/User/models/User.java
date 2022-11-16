package cow.starter.User.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import cow.starter.Appointment.models.Appointment;
import cow.starter.Auction.models.Auction;
import cow.starter.Bovine.models.Bovine;
import cow.starter.Field.models.Field;
import cow.starter.UserType.models.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="COW_User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;

    @Column(nullable = false)
    private String idContract;

    @Column(nullable = false)
    private String idWallet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_type")
    @JsonBackReference
    private UserType userType;

    @Column(nullable = false)
    private String name;

    private String fullName;

    private Date birthDate;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private double balance;

    private String imageCID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private Set<Appointment> appointmentSet = new HashSet();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private Set<Auction> auctionSet = new HashSet();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private Set<Bovine> bovineSet = new HashSet();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private Set<Field> fieldSet = new HashSet();

    public User(String idContract, String idWallet, UserType userType, String name, Date birthDate, String email,
                String password, Boolean active, Double balance, String fullName, String imageCID) {
        this.idContract = idContract;
        this.idWallet = idWallet;
        this.userType = userType;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.active = active;
        this.balance = balance;
        this.fullName = fullName;
        this.imageCID = imageCID;
    }
}
