/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.auction.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionCreateDTO {
    private long idBovine;
    private long idAuctioneer;
    private String auctionDescription;
    private Date startDate;
    private Date endDate;
    private double initialPrice;
    private int auctionStatus;
}
