package com.bernardo.figueiredo.cow.api.business.bid.boundary;

import com.bernardo.figueiredo.cow.api.business.bid.dto.Bid;
import com.bernardo.figueiredo.cow.api.business.bid.dto.BidDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BidMapper {
    public BidDTO mapEntityToDTO(Bid bid) {
        return new BidDTO(
                bid.getId(),
                bid.getIdContract(),
                bid.getAuction().getId(),
                bid.getBidder().getIdUser(),
                bid.getBidValue(),
                bid.getBidDate());
    }

    public List<BidDTO> mapSourceListToTargetList(List<Bid> sourceList) {
        List<BidDTO> targetList = new ArrayList<>();
        for (Bid bid : sourceList) {
            targetList.add(mapEntityToDTO(bid));
        }
        return targetList;
    }
}
