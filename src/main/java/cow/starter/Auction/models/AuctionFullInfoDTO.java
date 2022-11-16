package cow.starter.Auction.models;

import cow.starter.Bid.models.Bid;
import cow.starter.Bovine.models.Bovine;
import cow.starter.User.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionFullInfoDTO {
    private long idAuction;
    private String idContract;
    private Bovine bovine;
    private User user;
    private String auctionDescription;
    private Date startDate;
    private Date endDate;
    private int status;
    private double startingPrice;
    private Set<Bid> bidSet = new HashSet();
}
