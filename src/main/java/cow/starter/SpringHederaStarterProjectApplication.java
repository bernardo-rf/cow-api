package cow.starter;

import cow.starter.Bovine.models.BovineRepository;
import cow.starter.User.models.User;
import cow.starter.User.models.UserRepository;
import cow.starter.UserType.models.UserType;
import cow.starter.UserType.models.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SpringHederaStarterProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringHederaStarterProjectApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(UserTypeRepository userTypeRepository) {
		return args -> {
			List<UserType> userTypes = userTypeRepository.getAllUserTypes();
			if (userTypes.isEmpty()){
				userTypeRepository.save(new UserType("Farmer", true));
				userTypeRepository.save(new UserType("Veterinary", true));
				userTypeRepository.save(new UserType("Buyer", true));
			}
		};
	}



}
