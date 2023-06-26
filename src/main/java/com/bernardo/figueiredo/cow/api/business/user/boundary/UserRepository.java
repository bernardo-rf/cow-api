/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user.boundary;

import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id = :id and u.active = true")
    User getUserById(long id);

    @Query("SELECT u FROM User u WHERE u.email = :email and u.active = true")
    User getUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.idWallet = :idWallet and u.active = true")
    User getUserByWalletId(String idWallet);

    @Query("SELECT u FROM User u WHERE u.userType.id = :idUserType")
    List<User> getUsersByUserTypeId(long idUserType);
}
