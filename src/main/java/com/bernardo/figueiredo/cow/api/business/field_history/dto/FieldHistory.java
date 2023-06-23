/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.field_history.dto;

import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.field.dto.Field;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COW_FieldHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
}
