/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.bovine.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BovineDTO {
    private long idBovine;
    private String idContract;
    private String idOwner;
    private long idField;
    private long serialNumber;
    private String birthDate;
    private Double weight;
    private Double height;
    private String breed;
    private String color;
    private Boolean active;
    private String observation;
    private long idBovineParent1;
    private long idBovineParent2;
    private boolean gender;
    private String imageCID;
}
