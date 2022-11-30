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
