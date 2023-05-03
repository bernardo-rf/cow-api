/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field.dto;

import com.bernardo.figueiredo.cow.api.business.bovine.dto.BovineDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldDTO {
    private long idField;
    private String idOwner;
    private String idContract;
    private String fieldDescription;
    private Double latitude;
    private Double longitude;
    private String address;
    private int limit;
    private Boolean active;
    private String observation;
    private Set<BovineDTO> bovines;
}
