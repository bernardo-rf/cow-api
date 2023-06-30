/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user_type.dto;

import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "COW_UserType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    @Nationalized
    private String typeDescription;

    @Column(nullable = false)
    private Boolean active;

    public UserType(String description, Boolean active) {
        this.typeDescription = description;
        this.active = active;
    }
}
