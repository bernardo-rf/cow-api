/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.field_history.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldHistoryDTO {
    private long idFieldHistory;
    private long idField;
    private long idBovine;
    private Date switchDate;
}
