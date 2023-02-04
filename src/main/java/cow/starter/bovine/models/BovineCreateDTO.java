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

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BovineCreateDTO {
    private String idOwner;
    private long idField;
    private long serialNumber;
    private Date birthDate;
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
