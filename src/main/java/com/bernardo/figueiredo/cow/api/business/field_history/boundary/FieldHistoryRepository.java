/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field_history.boundary;

import com.bernardo.figueiredo.cow.api.business.field_history.dto.FieldHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FieldHistoryRepository extends JpaRepository<FieldHistory, Long> {

    @Query("SELECT f FROM FieldHistory f WHERE f.id = :id ORDER BY f.switchDate DESC")
    FieldHistory getFieldHistoryById(long id);

    @Query("SELECT f FROM FieldHistory f WHERE  f.bovine.idBovine = :idBovine ORDER BY f.switchDate DESC, .id DESC")
    List<FieldHistory> getFieldHistoryByBovineId(long idBovine);
}
