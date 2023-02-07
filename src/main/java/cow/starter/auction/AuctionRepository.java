package cow.starter.Auction;

import cow.starter.Auction.models.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("SELECT a FROM Auction a")
    List<Auction> getAllAuction();

    @Query("SELECT a FROM Auction a WHERE a.status = :status")
    List<Auction> getAuctionsByStatus(int status);

    @Query("SELECT a FROM Auction a WHERE a.idOwner = :idOwner")
    List<Auction> getAuctionsByIDOwner(String idOwner);

    @Query("SELECT a FROM Auction a WHERE a.idAuction = :idAuction")
    Auction getAuctionByIDAuction(long idAuction);

}
