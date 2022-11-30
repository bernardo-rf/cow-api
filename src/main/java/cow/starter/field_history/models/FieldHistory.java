package cow.starter.field_history.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import cow.starter.bovine.models.Bovine;
import cow.starter.field.models.Field;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="COW_FieldHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idFieldHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_field")
    @JsonBackReference
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bovine")
    @JsonBackReference
    private Bovine bovine;

    @Column(nullable = false)
    private Date switchDate;

    public FieldHistory(Field field, Bovine bovine, Date switchDate) {
        this.field = field;
        this.bovine=bovine;
        this.switchDate=switchDate;
    }
}
