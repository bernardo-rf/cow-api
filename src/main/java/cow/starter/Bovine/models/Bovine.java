package cow.starter.Bovine.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import cow.starter.Appointment.models.Appointment;
import cow.starter.Auction.models.Auction;
import cow.starter.Field.models.Field;
import cow.starter.User.models.User;
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
@Table(name = "COW_Bovine")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bovine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBovine;

    @Column(nullable = false)
    private String idContract;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_owner", referencedColumnName = "idWallet")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_field")
    @JsonBackReference
    private Field field;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bovine")
    @JsonManagedReference
    private Set<Appointment> appointmentSet = new HashSet();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bovine")
    @JsonManagedReference
    private Set<Auction> auctionSet = new HashSet();

    public Bovine(String idContract, User user, Field field, long serialNumber, Date birthDate, Double weight,
                  Double height, String breed, String color, Boolean active, String observation, long idBovineParent1,
                  long idBovineParent2, boolean gender, String imageCID) {
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
    }
}
