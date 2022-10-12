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
    @Column(name = "IDFieldHistory")
    @ApiModelProperty(example = "1", name = "idFieldHistory", required = true)
    private long idFieldHistory;

    @Column(name = "IDField", nullable = false)
    @ApiModelProperty(example = "1", name = "idField")
    private long idField;

    @Column(name = "IDBovine", nullable = false)
    @ApiModelProperty(example = "1", name = "idBovine")
    private long idBovine;

    @Column(name = "SwitchDate", nullable = false)
    @ApiModelProperty(example = "2022-01-01", name = "switchDate")
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
