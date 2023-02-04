/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.auction.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionCreateDTO {
    private long idBovine;
    private String idOwner;
    private String auctionDescription;
    private Date startDate;
    private Date endDate;
    private int status;
    private double startingPrice;
}