package cow.starter.FieldHistory.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldHistoryCreatedDTO {
    private long idField;
    private long idBovine;
    private Date switchDate;
}
