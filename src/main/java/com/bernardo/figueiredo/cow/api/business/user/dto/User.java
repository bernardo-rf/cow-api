/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user.dto;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.bernardo.figueiredo.cow.api.business.auction.dto.Auction;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.field.dto.Field;
import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "COW_User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;

    @Column(nullable = false, unique = true)
    @Nationalized
    private String idContract;

    @Column(nullable = false, unique = true)
    @Nationalized
    private String idWallet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_type")
    @JsonBackReference
    private UserType userType;

    @Column(nullable = false, length = 15)
    @Nationalized
    private String name;

    @Column(length = 150)
    @Nationalized
    private String fullName;

    private Date birthDate;

    @Column(nullable = false, unique = true, length = 50)
    @Nationalized
    private String email;

    @Column(nullable = false)
    @Nationalized
    private String password;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private double balance;

    @Nationalized
    private String imageCID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "veterinary")
    @JsonManagedReference
    private Set<Appointment> appointmentSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "auctioneer")
    @JsonManagedReference
    private Set<Auction> auctionSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @JsonManagedReference
    private Set<Bovine> bovineSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @JsonManagedReference
    private Set<Field> fieldSet = new HashSet<>();

    public User(
            String idContract,
            String idWallet,
            UserType userType,
            String name,
            Date birthDate,
            String email,
            String password,
            Boolean active,
            Double balance,
            String fullName,
            String imageCID) {
        this.idContract = idContract;
        this.idWallet = idWallet;
        this.userType = userType;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.active = active;
        this.balance = balance;
        this.fullName = fullName;
        this.imageCID = imageCID;
    }
}
