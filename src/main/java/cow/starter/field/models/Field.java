package cow.starter.field.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import cow.starter.bovine.models.Bovine;
import cow.starter.field_history.models.FieldHistory;
import cow.starter.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COW_Field")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Field implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idField;

    @Column(nullable = false, unique = true)
    private String idContract;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "idWallet")
    @JsonBackReference
    private User user;

    @Column(nullable = false, length = 100)
    @Nationalized
    private String fieldDescription;

    @Column(nullable = false, length = 250)
    @Nationalized
    private String address;

    @Column(nullable = false)
    private int fieldLimit;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Boolean active;

    @Nationalized
    private String observation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "field")
    @JsonManagedReference
    private Set<Bovine> bovineSet = new HashSet();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "field")
    @JsonManagedReference
    private Set<FieldHistory> fieldHistorySet = new HashSet();

    public Field(String idContract, User user, String fieldDescription, String address, int limit, Double latitude,
                 Double longitude, Boolean active, String observation) {
        this.idContract = idContract;
        this.user = user;
        this.fieldDescription = fieldDescription;
        this.address = address;
        this.fieldLimit = limit;
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
        this.observation = observation;
    }
}