package hedera.starter.cowUser.models;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1 and u.active = true")
    Optional<User> findUserByEmail(String userEmail);

    @Query("SELECT u FROM User u WHERE u.idWallet = ?1 and u.active = true")
    Optional<User> findUserByIDWallet(String idWallet);

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> getAllUsers();

    @Query("SELECT u FROM User u WHERE u.idUser = 1 and u.active = true")
    List<User> getUserByIDUser(long idUser);
}
