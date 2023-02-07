package cow.starter.Auction.models;

import cow.starter.Bid.models.Bid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="COW_Auction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Auction implements Serializable {

    /**
     * @param idAuction
     * @param idContract
     * @param idOwner
     * @param auctionDescription
     * @param startDate
     * @param endDate
     * @param status
     * @param breed
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAuction;

    @Column(nullable = false)
    private String idContract;

    @Column(nullable = false)
    private long idBovine;

    @Column(nullable = false)
    private String idOwner;

    @Column(nullable = false, length = 50)
    private String auctionDescription;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private int status;

    @Column(scale = 2)
    private double startingPrice;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "auction")
    private Set<Bid> bidSet = new HashSet();

    public Auction(String idContract, long idBovine, String idOwner, String auctionDescription, Date startDate,
                   Date endDate, int status, double startingPrice) {
        this.idContract = idContract;
        this.idBovine = idBovine;
        this.idOwner = idOwner;
        this.auctionDescription = auctionDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.startingPrice = startingPrice;
    }
}
