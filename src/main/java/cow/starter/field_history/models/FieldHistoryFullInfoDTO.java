package cow.starter.field_history.models;

public class FieldHistoryFullInfoDTO {
    private long idFieldHistory;
    private long idField;
    private String fieldDescription;
    private String fieldAddress;
    private long idBovine;
    private long bovineSerialNumber;
    private String switchDate;

    public FieldHistoryFullInfoDTO() { }

    public FieldHistoryFullInfoDTO(long idFieldHistory, long idField, String fieldDescription, String fieldAddress,
                                   long idBovine, long bovineSerialNumber, String switchDate) {
        this.idFieldHistory = idFieldHistory;
        this.idField = idField;
        this.fieldDescription = fieldDescription;
        this.fieldAddress=fieldAddress;
        this.idBovine = idBovine;
        this.bovineSerialNumber = bovineSerialNumber;
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

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public String getFieldAddress() {
        return fieldAddress;
    }

    public void setFieldAddress(String fieldAddress) {
        this.fieldAddress = fieldAddress;
    }

    public long getIdBovine() {
        return idBovine;
    }

    public void setIdBovine(long idBovine) {
        this.idBovine = idBovine;
    }

    public long getBovineSerialNumber() {
        return bovineSerialNumber;
    }

    public void setBovineSerialNumber(long bovineSerialNumber) {
        this.bovineSerialNumber = bovineSerialNumber;
    }

    public String getSwitchDate() {
        return switchDate;
    }

    public void setSwitchDate(String switchDate) {
        this.switchDate = switchDate;
    }
}
