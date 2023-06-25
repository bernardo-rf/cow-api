package com.bernardo.figueiredo.cow.api.business.bovine.boundary;

import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.BovineDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BovineMapper {
    public BovineDTO mapEntityToDTO(Bovine bovine) {
        return new BovineDTO(
                bovine.getId(),
                bovine.getIdContract(),
                bovine.getOwner().getIdUser(),
                bovine.getField().getId(),
                bovine.getSerialNumber(),
                bovine.getBirthDate(),
                bovine.getWeight(),
                bovine.getHeight(),
                bovine.getBreed(),
                bovine.getColor(),
                bovine.getActive(),
                bovine.getObservation(),
                bovine.getBovineParent1().getId(),
                bovine.getBovineParent2().getId(),
                bovine.isGender(),
                bovine.getImageCID());
    }

    public List<BovineDTO> mapSourceListToTargetList(List<Bovine> sourceList) {
        List<BovineDTO> targetList = new ArrayList<>();
        for (Bovine bovine : sourceList) {
            targetList.add(mapEntityToDTO(bovine));
        }
        return targetList;
    }
}
