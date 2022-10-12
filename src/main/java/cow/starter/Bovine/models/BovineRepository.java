package cow.starter.Bovine.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BovineRepository extends JpaRepository<Bovine, Long> {
    @Query("SELECT b FROM Bovine b WHERE b.active = true")
    List<Bovine> getAllBovine();

    @Query("SELECT b FROM Bovine b WHERE b.idOwner = :idOwner and b.active = true")
    List<Bovine> getAllBovineIdOwner(String idOwner);

    @Query("SELECT b FROM Bovine b WHERE b.idOwner = :idOwner and b.gender = true and b.idBovine <> :idBovine")
    List<Bovine> getAllBovineMaleIdOwner(String idOwner, long idBovine);

    @Query("SELECT b FROM Bovine b WHERE b.idOwner = :idOwner and b.gender = false and b.idBovine <> :idBovine")
    List<Bovine> getAllBovineFeminineIdOwner(String idOwner, long idBovine);

    @Query("SELECT b FROM Bovine b WHERE b.serialNumber = :serialNumber and b.active = true")
    Bovine checkBovineSerialNumber(long serialNumber);

    @Query("SELECT b FROM Bovine b WHERE b.idBovine = :idBovine and b.active = true ORDER BY b.idBovine ASC")
    Bovine getBovine(long idBovine);

    @Query( value ="WITH temp(idbovine,idbovine_parent1, idbovine_parent2, active, birth_date, breed, color, height, " +
            "idcontract, idfield, observation, serial_number, weight, gender, idowner, imagecid) as " +
            "(SELECT S.idbovine, S.idbovine_parent1, S.idbovine_parent2, S.active, S.birth_date, S.breed, S.color, " +
            "S.height, S.idcontract, S.idfield, S.observation, S.serial_number, S.weight, S.gender, S.idowner, S.imagecid " +
            "FROM cow_bovine as S " +
            "WHERE idbovine = ?1 " +
            "UNION ALL " +
            "SELECT S2.idbovine, S2.idbovine_parent1, S2.idbovine_parent2, S2.active, S2.birth_date, S2.breed, S2.color, " +
            "S2.height, S2.idcontract, S2.idfield, S2.observation, S2.serial_number, S2.weight, S2.gender, S2.idowner, S2.imagecid " +
            "FROM cow_bovine as S2 INNER JOIN temp on S2.idbovine=temp.idbovine_parent1 and temp.idbovine is not null " +
            "UNION ALL " +
            "SELECT S3.idbovine, S3.idbovine_parent1, S3.idbovine_parent2, S3.active, S3.birth_date, S3.breed, S3.color, " +
            "S3.height, S3.idcontract, S3.idfield, S3.observation, S3.serial_number, S3.weight, S3.gender, S3.idowner, S3.imagecid " +
            "FROM cow_bovine as S3 INNER JOIN temp on S3.idbovine=temp.idbovine_parent2 and temp.idbovine is not null) " +
            "SELECT DISTINCT * FROM temp order by idbovine",
            nativeQuery = true)
    List<Bovine> getGenealogy(long idBovine);


}
