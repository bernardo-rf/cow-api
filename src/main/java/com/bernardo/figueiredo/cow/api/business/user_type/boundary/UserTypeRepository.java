/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user_type.boundary;

import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {

    @Query("SELECT u FROM UserType u WHERE u.id = :id and u.active = true")
    UserType getUserTypeById(long id);

    @Query("SELECT u FROM UserType u WHERE u.typeDescription = :typeDescription")
    UserType getUserTypeByDescription(String typeDescription);

    @Query("SELECT u FROM UserType u WHERE u.active = true")
    List<UserType> getUserTypes();
}
