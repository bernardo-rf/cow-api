package cow.starter.Bovine.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BovineRepository extends JpaRepository<Bovine, Long> {
    @Query("SELECT b FROM Bovine b WHERE b.active = true")
    List<Bovine> getAllBovine();

    @Query("SELECT b FROM Bovine b WHERE b.idOwner = :idOwner and b.active = true")
    List<Bovine> getAllBovineIdOwner(String idOwner);

    @Query("SELECT b FROM Bovine b WHERE b.serialNumber = :serialNumber and b.active = true")
    Bovine checkBovineSerialNumber(long serialNumber);

    @Query("SELECT b FROM Bovine b WHERE b.idBovine = :idBovine and b.active = true ORDER BY b.idBovine ASC")
    Bovine getBovine(long idBovine);

}
