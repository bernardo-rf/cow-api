package cow.starter.Bid;

import cow.starter.Bid.models.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b FROM Bid b")
    List<Bid> getAllBids();

    @Query("SELECT b FROM Bid b WHERE b.auction.idAuction = :idAuction")
    List<Bid> getBidsByIDAuction(long idAuction);

    @Query("SELECT b FROM Bid b WHERE b.idBidder = :idOwner")
    List<Bid> getBidsByIDBidder(String idOwner);

    @Query("SELECT b FROM Bid b WHERE b.idBid = :idBid")
    Bid getBidByIDBid(long idBid);
}
