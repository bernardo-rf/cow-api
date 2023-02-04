/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.user_type.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    @Query("SELECT u FROM UserType u WHERE u.idUserType = :idUserType")
    UserType getUserType(long idUserType);

    @Query("SELECT u FROM UserType u WHERE u.active = true")
    List<UserType> getAllUserTypes();

}
