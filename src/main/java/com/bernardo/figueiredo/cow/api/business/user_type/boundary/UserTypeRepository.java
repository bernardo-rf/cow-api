/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user_type.boundary;

import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    @Query("SELECT u FROM UserType u WHERE u.idUserType = :idUserType")
    UserType getUserType(long idUserType);

    @Query("SELECT u FROM UserType u WHERE u.active = true")
    List<UserType> getAllUserTypes();

}
