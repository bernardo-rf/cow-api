/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.bid;

import cow.starter.bid.models.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b FROM Bid b")
    List<Bid> getAllBids();

    @Query("SELECT b FROM Bid b WHERE b.idBid = :idBid")
    Bid getBidByIDBid(long idBid);
}
