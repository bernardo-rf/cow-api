package cow.starter.Bovine.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BovineRepository extends JpaRepository<Bovine, Long> {
    @Query("SELECT b FROM Bovine b WHERE b.active = true")
    List<Bovine> getAllBovine();

    @Query("SELECT b FROM Bovine b WHERE b.user.idWallet = :idOwner and b.active = true ORDER BY b.serialNumber ASC")
    List<Bovine> getAllBovineIdOwner(String idOwner);

    @Query(value ="SELECT B.* FROM cow_bovine as B WHERE B.id_owner = :idOwner and B.id_bovine NOT IN (SELECT A.id_bovine FROM cow_auction as A WHERE A.status != 2) and B.active = 1 ORDER BY B.serial_number ASC", nativeQuery = true)
    List<Bovine> getAllBovineIdOwnerToAuction(String idOwner);

    @Query("SELECT b FROM Bovine b WHERE b.user.idWallet = :idOwner and b.gender = true and b.idBovine <> :idBovine")
    List<Bovine> getAllBovineMaleIdOwner(String idOwner, long idBovine);

    @Query("SELECT b FROM Bovine b WHERE b.user.idWallet = :idOwner and b.gender = false and b.idBovine <> :idBovine")
    List<Bovine> getAllBovineFeminineIdOwner(String idOwner, long idBovine);

    @Query(" SELECT b FROM Bovine b LEFT JOIN Field f ON b.field.idField = f.idField " +
            "WHERE b.field.idField <> :idField and b.user.idWallet = :idWallet and b.active = true")
    List<Bovine> getAllBovinesNotIn(long idField, String idWallet);

    @Query("SELECT b FROM Bovine b WHERE b.serialNumber = :serialNumber and b.user.idWallet = :idWallet and b.active = true")
    Bovine checkBovineSerialNumber(long serialNumber, String idWallet);

    @Query("SELECT b FROM Bovine b WHERE b.idBovine = :idBovine and b.active = true ORDER BY b.idBovine ASC")
    Bovine getBovine(long idBovine);


    @Query( value ="WITH temp(id_bovine,id_bovine_parent1, id_bovine_parent2, active, birth_date, breed, color, height, " +
            "id_contract, id_field, observation, serial_number, weight, gender, id_owner, imagecid) as " +
            "(SELECT S.id_bovine, S.id_bovine_parent1, S.id_bovine_parent2, S.active, S.birth_date, S.breed, S.color, " +
            "S.height, S.id_contract, S.id_field, S.observation, S.serial_number, S.weight, S.gender, S.id_owner, S.imagecid " +
            "FROM cow_bovine as S " +
            "WHERE id_bovine = ?1 " +
            "UNION ALL " +
            "SELECT S2.id_bovine, S2.id_bovine_parent1, S2.id_bovine_parent2, S2.active, S2.birth_date, S2.breed, S2.color, " +
            "S2.height, S2.id_contract, S2.id_field, S2.observation, S2.serial_number, S2.weight, S2.gender, S2.id_owner, S2.imagecid " +
            "FROM cow_bovine as S2 INNER JOIN temp on S2.id_bovine=temp.id_bovine_parent1 and temp.id_bovine is not null " +
            "UNION ALL " +
            "SELECT S3.id_bovine, S3.id_bovine_parent1, S3.id_bovine_parent2, S3.active, S3.birth_date, S3.breed, S3.color, " +
            "S3.height, S3.id_contract, S3.id_field, S3.observation, S3.serial_number, S3.weight, S3.gender, S3.id_owner, S3.imagecid " +
            "FROM cow_bovine as S3 INNER JOIN temp on S3.id_bovine=temp.id_bovine_parent2 and temp.id_bovine is not null) " +
            "SELECT DISTINCT * FROM temp order by id_bovine",
            nativeQuery = true)
    List<Bovine> getGenealogy(long idBovine);


}
