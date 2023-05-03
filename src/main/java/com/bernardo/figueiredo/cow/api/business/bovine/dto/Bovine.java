/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.bovine.dto;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.bernardo.figueiredo.cow.api.business.auction.dto.Auction;
import com.bernardo.figueiredo.cow.api.business.field.dto.Field;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COW_Bovine")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bovine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBovine;

    @Column(nullable = false)
    @Nationalized
    private String idContract;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "idWallet")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_field")
    @JsonBackReference
    private Field field;

    @Column(nullable = false)
    private long serialNumber;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false, length = 50)
    @Nationalized
    private String breed;

    @Column(nullable = false, length = 50)
    private String color;

    @Column(nullable = false)
    private Boolean active;

    @Nationalized
    private String observation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bovine_parent1")
    @JsonBackReference
    private Bovine bovineParent1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bovine_parent2")
    @JsonBackReference
    private Bovine bovineParent2;

    @Column(nullable = false)
    private boolean gender;

    @Nationalized
    private String imageCID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bovine")
    @JsonManagedReference
    private Set<Appointment> appointmentSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bovine")
    @JsonManagedReference
    private Set<Auction> auctionSet = new HashSet<>();

    public Bovine(String idContract, User user, Field field, long serialNumber, Date birthDate, Double weight,
                  Double height, String breed, String color, Boolean active, String observation, Bovine bovineParent1,
                  Bovine bovineParent2, boolean gender, String imageCID) {
        this.idContract = idContract;
        this.user = user;
        this.field = field;
        this.serialNumber = serialNumber;
        this.birthDate = birthDate;
        this.weight = weight;
        this.height = height;
        this.breed = breed;
        this.color = color;
        this.active = active;
        this.observation = observation;
        this.bovineParent1 = bovineParent1;
        this.bovineParent2 = bovineParent2;
        this.gender = gender;
        this.imageCID = imageCID;
    }
}
