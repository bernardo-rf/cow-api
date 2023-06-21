/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.bid.boundary;

import com.bernardo.figueiredo.cow.api.business.bid.dto.Bid;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b FROM Bid b ORDER BY bidDate DESC")
    List<Bid> getBids();

    @Query("SELECT b FROM Bid b WHERE b.id = :id ORDER BY bidDate DESC")
    Bid getBidByBidId(long id);

    @Query("SELECT b FROM Bid b WHERE b.auction.id = :id ORDER BY bidDate DESC")
    List<Bid> getBidByAuctionId(long id);
}
