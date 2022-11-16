package cow.starter.Bid.models;

import cow.starter.Auction.models.Auction;
import cow.starter.User.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidFullInfoDTO {
    private long idBid;
    private String idContract;
    private Auction auction;
    private User user;
    private Double value;
    private Date bidDate;
}
