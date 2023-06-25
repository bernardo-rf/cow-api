/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldDTO {
    private long id;
    private long idOwner;
    private String idContract;
    private String fieldDescription;
    private Double latitude;
    private Double longitude;
    private String address;
    private int maxCapacityLimit;
    private Boolean active;
    private String observation;
}
