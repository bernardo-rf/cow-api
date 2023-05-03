/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user.boundary;

import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> getAllUsers();

    @Query("SELECT u FROM User u " +
            "LEFT JOIN UserType ut ON ut.idUserType = u.userType.idUserType " +
            "WHERE u.active = true and ut.typeDescription='Veterinary'")
    List<User> getAllUsersVeterinary();

    @Query("SELECT u FROM User u WHERE u.idUser = :idUser and u.active = true")
    User getUser(long idUser);

    @Query("SELECT u FROM User u WHERE u.idWallet = :idOwner and u.active = true")
    User getUserByIDOwner(String idOwner);

    @Query("SELECT u FROM User u WHERE u.email = :userEmail and u.active = true and u.idUser <> :idUser")
    User getUserByEmail(String userEmail, long idUser);

    @Query("SELECT u FROM User u WHERE u.idWallet = ?1 and u.active = true")
    User getUserByIDWallet(String idWallet);
}
