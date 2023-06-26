package com.bernardo.figueiredo.cow.api.business.user_type.boundary;

import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserType;
import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserTypeDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserTypeMapper {

    public UserTypeDTO mapEntityToDTO(UserType userType) {
        return new UserTypeDTO(userType.getId(), userType.getTypeDescription(), userType.getActive());
    }

    public List<UserTypeDTO> mapSourceListToTargetList(List<UserType> sourceList) {
        List<UserTypeDTO> targetList = new ArrayList<>();
        for (UserType userType : sourceList) {
            targetList.add(mapEntityToDTO(userType));
        }
        return targetList;
    }
}
