package cow.starter.FieldHistory.models;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="COW_FieldHistory")
public class FieldHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "1", name = "idFieldHistory", required = true)
    private long idFieldHistory;

    @Column(nullable = false)
    @ApiModelProperty(example = "1", name = "idField", required = true)
    private long idField;

    @Column(nullable = false)
    @ApiModelProperty(example = "1", name = "idBovine", required = true)
    private long idBovine;

    @Column(nullable = false)
    @ApiModelProperty(example = "2022-01-01", name = "switchDate", required = true)
    private Date switchDate;


    public FieldHistory() { super(); }

    public FieldHistory(long idField, long idBovine, Date switchDate) {
        this.idField = idField;
        this.idBovine=idBovine;
        this.switchDate=switchDate;
    }

    public long getIdFieldHistory() {
        return idFieldHistory;
    }

    public void setIdFieldHistory(long idFieldHistory) {
        this.idFieldHistory = idFieldHistory;
    }

    public long getIdField() {
        return idField;
    }

    public void setIdField(long idField) {
        this.idField = idField;
    }

    public long getIdBovine() {
        return idBovine;
    }

    public void setIdBovine(long idBovine) {
        this.idBovine = idBovine;
    }

    public Date getSwitchDate() {
        return switchDate;
    }

    public void setSwitchDate(Date switchDate) {
        this.switchDate = switchDate;
    }
}
