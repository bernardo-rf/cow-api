package com.bernardo.figueiredo.cow.api.business.auction.boundary;

import com.bernardo.figueiredo.cow.api.business.auction.dto.Auction;
import com.bernardo.figueiredo.cow.api.business.auction.dto.AuctionDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuctionMapper {
    public AuctionDTO mapEntityToDTO(Auction auction) {
        return new AuctionDTO(
                auction.getId(),
                auction.getIdContract(),
                auction.getBovine().getIdBovine(),
                auction.getAuctioneer().getIdUser(),
                auction.getHighestBidder().getIdUser(),
                auction.getAuctionDescription(),
                auction.getStartDate(),
                auction.getEndDate(),
                auction.getInitialPrice(),
                auction.getAuctionStatus());
    }

    public List<AuctionDTO> mapSourceListToTargetList(List<Auction> sourceList) {
        List<AuctionDTO> targetList = new ArrayList<>();
        for (Auction auction : sourceList) {
            targetList.add(mapEntityToDTO(auction));
        }
        return targetList;
    }
}
