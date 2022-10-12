package cow.starter.FieldHistory.models;

import java.util.Date;

public class FieldHistoryDTO {
    private long idFieldHistory;
    private long idField;
    private long idBovine;
    private Date switchDate;

    public FieldHistoryDTO() { }

    public FieldHistoryDTO(long idFieldHistory, long idField, long idBovine, Date switchDate) {
        this.idFieldHistory = idFieldHistory;
        this.idField = idField;
        this.idBovine = idBovine;
        this.switchDate = switchDate;
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
