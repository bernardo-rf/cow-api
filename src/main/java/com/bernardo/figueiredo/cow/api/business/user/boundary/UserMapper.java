package com.bernardo.figueiredo.cow.api.business.user.boundary;

import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.bernardo.figueiredo.cow.api.business.user.dto.UserAuth;
import com.bernardo.figueiredo.cow.api.business.user.dto.UserAuthDTO;
import com.bernardo.figueiredo.cow.api.business.user.dto.UserDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserDTO mapEntityToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getIdContract(),
                user.getIdWallet(),
                user.getUserType().getId(),
                user.getName(),
                user.getBirthDate(),
                user.getEmail(),
                user.getPassword(),
                user.getActive(),
                user.getBalance(),
                user.getFullName(),
                user.getImageCID());
    }

    public UserAuthDTO mapEntityToAuthDTO(UserAuth userAuth) {
        UserDTO userDTO = mapEntityToDTO(userAuth.getUser());
        return new UserAuthDTO(userAuth.getToken(), userDTO);
    }

    public List<UserDTO> mapSourceListToTargetList(List<User> sourceList) {
        List<UserDTO> targetList = new ArrayList<>();
        for (User user : sourceList) {
            targetList.add(mapEntityToDTO(user));
        }
        return targetList;
    }
}
