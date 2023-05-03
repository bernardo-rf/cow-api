/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field_history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldHistoryFullInfoDTO {
    private long idFieldHistory;
    private long idField;
    private String fieldDescription;
    private String fieldAddress;
    private long idBovine;
    private long bovineSerialNumber;
    private String switchDate;
}
