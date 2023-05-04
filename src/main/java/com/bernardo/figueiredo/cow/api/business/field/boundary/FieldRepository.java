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
    @Query("SELECT f FROM Field f WHERE f.active = true ORDER BY f.idField ASC")
    List<Field> getFields();

    @Query("SELECT f FROM Field f WHERE f.active = true and f.user.idWallet = :idOwner ORDER BY f.idField ASC")
    List<Field> getFieldsByIDOwner(String idOwner);

    @Query("SELECT f FROM Field f WHERE f.idField = :idField and f.active = true")
    Field getField(long idField);

    @Query(
            "SELECT f.fieldDescription FROM Field f WHERE f.fieldDescription = :fieldDescription and f.user.idWallet = :idOwner")
    Field checkFieldByAddressAndIDOwner(String fieldDescription, String idOwner);
}
