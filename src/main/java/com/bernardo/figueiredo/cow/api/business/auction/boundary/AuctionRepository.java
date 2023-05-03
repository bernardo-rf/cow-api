/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.auction.boundary;

import com.bernardo.figueiredo.cow.api.business.auction.dto.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("SELECT a FROM Auction a ORDER BY a.auctionStatus")
    List<Auction> getAllAuction();

    @Query("SELECT a FROM Auction a WHERE a.idAuction = :idAuction")
    Auction getAuctionByIDAuction(long idAuction);

}
