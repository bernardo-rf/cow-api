package hedera.starter.cowUserType.models;

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
    @Column(name = "IDUserType")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "1", name = "idUserType", required = true)
    private Integer idUserType;

    @Column(name = "Description")
    @ApiModelProperty(example = "Veterinary", name = "description", required = true)
    private String description;

    @Column(name = "Active")
    @ApiModelProperty(example = "1", name = "active", required = true)
    private Boolean active;

    public UserType() { super(); }

    public UserType(String description,  Boolean active) {
        this.description = description;
        this.active = active;
    }

    public Integer getIdUserType() {
        return idUserType;
    }

    public void setIdUserType(Integer idUserType) {
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
