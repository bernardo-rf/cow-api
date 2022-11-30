package cow.starter.field.models;

import cow.starter.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldFullInfoDTO {
    private long idField;
    private User user;
    private String idContract;
    private String fieldDescription;
    private Double latitude;
    private Double longitude;
    private String address;
    private int limit;
    private Boolean active;
    private String observation;
    private int currentOccupation;
    private int currentOccupationPercentage;
}
