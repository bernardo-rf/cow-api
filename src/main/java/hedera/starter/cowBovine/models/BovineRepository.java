package hedera.starter.cowBovine.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BovineRepository extends JpaRepository<Bovine, Long> {
    @Query("SELECT b FROM Bovine b WHERE b.active = true ORDER BY b.idBovine ASC")
    List<Bovine> getAllBovine();

    @Query("SELECT b FROM Bovine b WHERE b.idBovine = ?1 and b.active = true ")
    Optional<Bovine> getBovineByIDBovine(long idBovine);

    @Query("SELECT b FROM Bovine b WHERE b.idOwner = ?1 and b.active = true")
    List<Bovine> getBovineByIdUser(String IDOwner);

    @Query("SELECT b FROM Bovine b WHERE b.serialNumber = ?1 and b.active = true")
    Bovine checkBovineSerialNumber(String serialNumber);
}
