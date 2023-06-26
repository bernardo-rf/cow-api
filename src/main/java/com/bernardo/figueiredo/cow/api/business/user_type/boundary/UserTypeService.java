/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user_type.boundary;

import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTypeService {

    @Autowired
    private UserTypeRepository userTypeRepository;

    public UserType getUserTypeById(long id) {
        UserType userType = userTypeRepository.getUserTypeById(id);

        if (userType == null) {
            throw new ErrorCodeException(ErrorCode.USER_TYPE_NOT_FOUND);
        }

        return userType;
    }

    public UserType getUserTypeByDescription(String userTypeDescription) {
        UserType userType = userTypeRepository.getUserTypeByDescription(userTypeDescription);

        if (userType == null) {
            throw new ErrorCodeException(ErrorCode.USER_TYPE_NOT_FOUND);
        }

        return userType;
    }

    public List<UserType> getUserTypes() {
        List<UserType> userTypes = userTypeRepository.getUserTypes();

        if (userTypes.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.USER_TYPE_NOT_FOUND);
        }

        return userTypes;
    }

    public UserType createUserType(String userTypeDescription) {

        UserType userType = getUserTypeByDescription(userTypeDescription);
        if (userType != null) {
            throw new ErrorCodeException(ErrorCode.USER_TYPE_DESCRIPTION_INVALID);
        }

        UserType newUserType = new UserType(userTypeDescription, true);
        return userTypeRepository.save(newUserType);
    }
}
