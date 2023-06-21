/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.bid.dto;

import com.bernardo.figueiredo.cow.api.business.auction.dto.Auction;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "COW_Bid")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bid implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Nationalized
    private String idContract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_auction")
    @JsonBackReference
    private Auction auction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "idWallet")
    @JsonBackReference
    private User bidder;

    @Column(nullable = false, scale = 2)
    private double bidValue;

    @Column(nullable = false)
    private Date bidDate;

    public Bid(String idContract, Auction auction, User user, Double bidValue, Date bidDate) {
        this.idContract = idContract;
        this.auction = auction;
        this.bidder = user;
        this.bidValue = bidValue;
        this.bidDate = bidDate;
    }
}
