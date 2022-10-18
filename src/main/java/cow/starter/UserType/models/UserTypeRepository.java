package cow.starter.UserType.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    @Query("SELECT u FROM UserType u WHERE u.idUserType = ?1")
    UserType getUserType(int idUserType);

    @Query("SELECT u FROM UserType u WHERE u.description = ?1")
    UserType getUserTypeByDescription(String description);

    @Query("SELECT u FROM UserType u WHERE u.active = true")
    List<UserType> getAllUserTypes();

}
