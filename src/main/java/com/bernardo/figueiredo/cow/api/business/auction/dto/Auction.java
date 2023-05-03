/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.auction.dto;

import com.bernardo.figueiredo.cow.api.business.bid.dto.Bid;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name="COW_Auction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Auction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAuction;

    @Column(nullable = false, unique = true)
    @Nationalized
    private String idContract;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bovine")
    @JsonBackReference
    private Bovine bovine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "idWallet")
    @JsonBackReference
    private User user;

    @Column(nullable = false, length = 50)
    @Nationalized
    private String auctionDescription;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private int auctionStatus;

    @Column(scale = 2)
    private double startingPrice;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "auction")
    @JsonManagedReference
    private Set<Bid> bidSet = new HashSet<>();

    public Auction(String idContract, Bovine bovine, User user, String auctionDescription, Date startDate,
                   Date endDate, int auctionStatus, double startingPrice) {
        this.idContract = idContract;
        this.bovine = bovine;
        this.user = user;
        this.auctionDescription = auctionDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.auctionStatus = auctionStatus;
        this.startingPrice = startingPrice;
    }
}
