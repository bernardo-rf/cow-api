package cow.starter.FieldHistory.models;

import cow.starter.Field.models.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface FieldHistoryRepository extends JpaRepository<FieldHistory, Long> {

    @Query("SELECT f FROM FieldHistory f")
    List<Field> getAllFieldsHistory();

    @Query("SELECT f FROM FieldHistory f WHERE f.idField = :idField")
    List<FieldHistory> getAllFieldsHistoryByIDField(long idField);

    @Query("SELECT f FROM FieldHistory f WHERE  f.idBovine = :idBovine ORDER BY f.idFieldHistory DESC")
    List<FieldHistory> getAllFieldsHistoryByIDBovine(long idBovine);

    @Query("SELECT f FROM FieldHistory f WHERE f.idFieldHistory = :idFieldHistory")
    FieldHistory getFieldHistory(long idFieldHistory);
}
