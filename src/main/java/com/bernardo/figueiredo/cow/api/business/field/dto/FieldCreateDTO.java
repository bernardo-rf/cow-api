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
public class FieldCreateDTO {
    private String idOwner;
    private String fieldDescription;
    private String address;
    private Double latitude;
    private Double longitude;
    private int limit;
    private Boolean active;
    private String observation;
    private Set<BovineDTO> bovines;
}
