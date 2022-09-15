package hedera.starter.cowUserType.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    @Query("SELECT u FROM UserType u WHERE u.idUserType = ?1")
    Optional<UserType> getUserTypeByIDUserType(int idUserType);
}
