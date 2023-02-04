/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter;

import cow.starter.user_type.models.UserType;
import cow.starter.user_type.models.UserTypeRepository;
import cow.starter.utilities.Constants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SpringHederaStarterProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringHederaStarterProjectApplication.class, args);
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
