/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.field_history.models;

import cow.starter.field.models.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface FieldHistoryRepository extends JpaRepository<FieldHistory, Long> {

    @Query("SELECT f FROM FieldHistory f WHERE  f.bovine.idBovine = :idBovine ORDER BY f.idFieldHistory DESC")
    List<FieldHistory> getAllFieldsHistoryByIDBovine(long idBovine);

    @Query("SELECT f FROM FieldHistory f WHERE f.idFieldHistory = :idFieldHistory")
    FieldHistory getFieldHistory(long idFieldHistory);
}
