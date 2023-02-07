package cow.starter.Field.models;

import cow.starter.Bovine.models.Bovine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface FieldRepository extends JpaRepository<Field, Long> {
    @Query("SELECT f FROM Field f WHERE f.active = true ORDER BY f.idField ASC")
    List<Field> getAllFields();

    @Query("SELECT f FROM Field f WHERE f.active = true and f.idOwner = :idOwner ORDER BY f.idField ASC")
    List<Field> getAllFieldsByOwner(String idOwner);

    @Query("SELECT f FROM Field f WHERE f.idField = :idField and f.active = true")
    Field getField(long idField);

    @Query("SELECT f.fieldDescription FROM Field f " +
            "WHERE f.fieldDescription = :fieldDescription and f.idOwner = :idOwner")
    Field checkFieldByAddressAndIDOwner(String fieldDescription, String idOwner);

    @Query(" SELECT b " +
            "FROM Field f " +
            "LEFT JOIN Bovine b ON b.idField = f.idField " +
            "WHERE b.idField = :idField and b.active = true")
    List<Bovine> getFieldBovines(long idField);

    @Query(" SELECT b " +
            "FROM Field f " +
            "LEFT JOIN Bovine b ON b.idField = f.idField " +
            "WHERE b.idField <> :idField and b.active = true")
    List<Bovine> getAllBovinesNotIn(long idField);

    @Query(" SELECT (SELECT COUNT(b) FROM Bovine b WHERE b.idField = f.idField) As currentOccupation " +
            "FROM Field f " +
            "WHERE f.active = true " +
            "ORDER BY f.idField ASC")
    List<Integer> getAllFieldsCurrentOccupation();

    @Query(" SELECT (SELECT COUNT(b) FROM Bovine b WHERE b.idField = f.idField) As currentOccupation " +
            "FROM Field f " +
            "WHERE f.active = true and f.idField = :idField " +
            "ORDER BY f.idField ASC")
    int getFieldCurrentOccupation(long idField);



}
