package cow.starter.field.models;

import cow.starter.bovine.models.BovineDTO;
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
