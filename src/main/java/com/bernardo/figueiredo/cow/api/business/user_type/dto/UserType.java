/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user_type.dto;

import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COW_UserType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUserType;

    @Column(nullable = false, length = 100)
    @Nationalized
    private String typeDescription;

    @Column(nullable = false)
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userType")
    @JsonManagedReference
    private Set<User> users = new HashSet<>();

    public UserType(String description, Boolean active) {
        this.typeDescription = description;
        this.active = active;
    }
}
