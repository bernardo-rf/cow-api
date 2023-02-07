package cow.starter.UserType.models;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="COW_UserType")
public class UserType implements Serializable {
    /**
     * @param idUserType
     * @param description
     * @param active
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "1", name = "idUserType", required = true)
    private long idUserType;

    @Column(nullable = false)
    @ApiModelProperty(example = "Veterinary", name = "description", required = true)
    private String description;

    @Column(nullable = false)
    @ApiModelProperty(example = "1", name = "active", required = true)
    private Boolean active;

    public UserType() { super(); }

    public UserType(String description,  Boolean active) {
        this.description = description;
        this.active = active;
    }

    public long getIdUserType() {
        return idUserType;
    }

    public void setIdUserType(long idUserType) {
        this.idUserType = idUserType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
