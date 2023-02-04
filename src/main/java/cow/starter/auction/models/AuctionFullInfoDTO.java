/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.auction.models;

import cow.starter.bid.models.Bid;
import cow.starter.bovine.models.Bovine;
import cow.starter.user.models.User;
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
    private Set<Bid> bidSet = new HashSet<>();
}
