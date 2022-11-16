package cow.starter.Bid.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import cow.starter.Auction.models.Auction;
import cow.starter.User.models.User;
import lombok.*;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBid;

    @Column(nullable = false)
    private String idContract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_auction")
    @JsonBackReference
    private Auction auction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "idWallet")
    @JsonBackReference
    private User user;

    @Column(nullable = false, scale = 2)
    private Double value;

    @Column(nullable = false)
    private Date bidDate;

    public Bid(String idContract, Auction auction, User user, Double value, Date bidDate) {
        this.idContract = idContract;
        this.auction = auction;
        this.user = user;
        this.value = value;
        this.bidDate = bidDate;
    }
}


