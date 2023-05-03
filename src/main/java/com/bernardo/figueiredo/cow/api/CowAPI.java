/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api;

import com.bernardo.figueiredo.cow.api.business.user_type.dto.UserType;
import com.bernardo.figueiredo.cow.api.business.user_type.boundary.UserTypeRepository;
import com.bernardo.figueiredo.cow.api.utils.Constants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CowAPI {

    public static void main(String[] args) {
        SpringApplication.run(CowAPI.class, args);
    }

    @Bean
    public CommandLineRunner demoData(UserTypeRepository userTypeRepository) {
        return args -> {
            List<UserType> userTypes = userTypeRepository.getAllUserTypes();
            if (userTypes.isEmpty()) {
                userTypeRepository.save(new UserType(Constants.FARMER_USER_TYPE, true));
                userTypeRepository.save(new UserType(Constants.VETERINARY_USER_TYPE, true));
                userTypeRepository.save(new UserType(Constants.BUYER_USER_TYPE, true));
            }
        };
    }


}
