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

    @Query("SELECT b FROM Bid b")
    List<Bid> getAllBids();

    @Query("SELECT b FROM Bid b WHERE b.idBid = :idBid")
    Bid getBidByIDBid(long idBid);
}
