package com.bernardo.figueiredo.cow.api.business.bovine.boundary;

import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.BovineDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BovineMapper {
    public BovineDTO mapEntityToDTO(Bovine bovine) {

        long idBovineParent1 = 0;
        if(bovine.getBovineParent1() != null) {
            idBovineParent1 = bovine.getBovineParent1().getId();
        }

        long idBovineParent2 = 0;
        if(bovine.getBovineParent2() != null) {
            idBovineParent2 = bovine.getBovineParent2().getId();
        }

        return new BovineDTO(
                bovine.getId(),
                bovine.getIdContract(),
                bovine.getIdToken(),
                bovine.getOwner().getId(),
                bovine.getField().getId(),
                bovine.getSerialNumber(),
                bovine.getBirthDate(),
                bovine.getWeight(),
                bovine.getHeight(),
                bovine.getBreed(),
                bovine.getColor(),
                bovine.getActive(),
                bovine.getObservation(),
                idBovineParent1,
                idBovineParent2,
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
