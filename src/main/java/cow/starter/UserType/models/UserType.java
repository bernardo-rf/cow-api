package cow.starter.UserType.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import cow.starter.User.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COW_UserType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUserType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userType")
    @JsonManagedReference
    private Set<User> users = new HashSet();

    public UserType(String description, Boolean active) {
        this.description = description;
        this.active = active;
    }
}
