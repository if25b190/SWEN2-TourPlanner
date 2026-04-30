package at.fhtw.tourplanner;

import at.fhtw.tourplanner.model.Account;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

@SpringBootTest
@Order(1)
public class TourplannerApplicationTests {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	public static class UserMockConfig {
		@Bean
		@Primary
		public UserDetailsService userDetailsService() {
			UserDetails user = Account.builder()
					.uuid(UUID.randomUUID())
					.username("TestUser")
					.password("testtest")
					.build();
			UserDetails anotherUser = Account.builder()
					.uuid(UUID.randomUUID())
					.username("AnotherUser")
					.password("testtest")
					.build();
			return username -> switch (username) {
				case "User" -> user;
				case "AnotherUser" -> anotherUser;
				default -> null;
			};
		}
	}
}
