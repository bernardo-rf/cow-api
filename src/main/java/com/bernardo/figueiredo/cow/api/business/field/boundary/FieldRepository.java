/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field.boundary;

import com.bernardo.figueiredo.cow.api.business.field.dto.Field;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FieldRepository extends JpaRepository<Field, Long> {

    @Query("SELECT f FROM Field f WHERE f.id = :id and f.active = true")
    Field getFieldById(long id);

    @Query("SELECT f FROM Field f WHERE f.active = true and f.owner.idWallet = :idWallet ORDER BY f.id ASC")
    List<Field> getFieldsByUserWalletId(String idWallet);

    @Query(
            "SELECT f.address FROM Field f WHERE f.fieldDescription = :fieldDescription and f.owner.idWallet = :idWallet")
    Field getFieldAddressByDescriptionAndUserWalletId(String fieldDescription, String idWallet);
}
