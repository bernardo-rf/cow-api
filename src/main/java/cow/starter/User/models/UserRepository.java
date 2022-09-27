package cow.starter.User.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> getAllUsers();

    @Query("SELECT u FROM User u WHERE u.idUser = :idUser and u.active = true")
    User getUser(long idUser);

    @Query("SELECT u FROM User u WHERE u.idWallet = :idOwner and u.active = true")
    User getUserByIDOwner(String idOwner);

    @Query("SELECT u FROM User u WHERE u.email = ?1 and u.active = true")
    User getUserByEmail(String userEmail);

    @Query("SELECT u FROM User u WHERE u.idWallet = ?1 and u.active = true")
    User getUserByIDWallet(String idWallet);
}
