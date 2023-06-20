/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.auction.boundary;

import com.bernardo.figueiredo.cow.api.business.auction.dto.Auction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("SELECT a FROM Auction a WHERE a.id = :id")
    Auction getAuctionById(long id);

    @Query("SELECT a FROM Auction a ORDER BY a.auctionStatus ASC, a.startDate ASC")
    List<Auction> getAuctions();
}
