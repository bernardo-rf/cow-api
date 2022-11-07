package cow.starter.Bid.models;

import cow.starter.Auction.models.Auction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "COW_Bid")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bid implements Serializable {

    /**
     * @param idBid
     * @param idContract
     * @param idAuction
     * @param idBidder
     * @param value
     * @param bidDate
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBid;

    @Column(nullable = false)
    private String idContract;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_auction")
    private Auction auction;

    @Column(nullable = false)
    private String idBidder;

    @Column(nullable = false, scale = 2)
    private Double value;

    @Column(nullable = false)
    private Date bidDate;

    public Bid(String idContract, Auction auction, String idBidder, Double value, Date bidDate) {
        this.idContract = idContract;
        this.auction = auction;
        this.idBidder = idBidder;
        this.value = value;
        this.bidDate = bidDate;
    }
}


