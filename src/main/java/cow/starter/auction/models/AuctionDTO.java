package cow.starter.Auction.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionDTO {
    private long idAuction;
    private long idBovine;
    private String idContract;
    private String idOwner;
    private String auctionDescription;
    private Date startDate;
    private Date endDate;
    private int status;
    private double startingPrice;
}
