package cow.starter.Bid.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidDTO {
    private long idBid;
    private String idContract;
    private long idAuction;
    private String idBidder;
    private  Double value;
    private Date bidDate;
}
