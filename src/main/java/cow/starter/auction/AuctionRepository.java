package cow.starter.auction;

import cow.starter.auction.models.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("SELECT a FROM Auction a ORDER BY a.auctionStatus")
    List<Auction> getAllAuction();

    @Query("SELECT a FROM Auction a WHERE a.auctionStatus = :status")
    List<Auction> getAuctionsByStatus(int status);

    @Query("SELECT a FROM Auction a WHERE a.user.idWallet = :idOwner")
    List<Auction> getAuctionsByIDOwner(String idOwner);

    @Query("SELECT a FROM Auction a WHERE a.idAuction = :idAuction")
    Auction getAuctionByIDAuction(long idAuction);

}
