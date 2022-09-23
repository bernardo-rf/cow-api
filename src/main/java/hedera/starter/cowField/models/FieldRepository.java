package hedera.starter.cowField.models;

import hedera.starter.cowBovine.models.Bovine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long> {
    @Query("SELECT f FROM Field f WHERE f.active = true ORDER BY f.idField ASC")
    List<Field> getAllFields();

    @Query("SELECT f FROM Field f WHERE f.active = true and f.idOwner = :idOwner ORDER BY f.idField ASC")
    List<Field> getAllFieldsByOwner(String idOwner);

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


    @Query("SELECT f FROM Field f WHERE f.idField = ?1 and f.active = true")
    Optional<Field> getFieldByIDField(long idField);

    @Query(" SELECT b " +
            "FROM Field f " +
            "LEFT JOIN Bovine b ON b.idField = f.idField " +
            "WHERE b.idField = :idField and b.active = true")
    List<Bovine> getFieldBovine(long idField);

    @Query(" SELECT b " +
            "FROM Field f " +
            "LEFT JOIN Bovine b ON b.idField = f.idField " +
            "WHERE b.idField <> :idField and b.active = true")
    List<Bovine> getAllBovineNotIn(long idField);

    @Query("SELECT f.fieldDescription FROM Field f " +
            "WHERE f.fieldDescription = :fieldDescription and f.idOwner = :idOwner")
    Field findFieldByAddress(String fieldDescription, String idOwner);

}
