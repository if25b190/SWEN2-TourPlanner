package at.fhtw.tourplanner;

import at.fhtw.tourplanner.config.AppConfig;
import at.fhtw.tourplanner.properties.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AppConfig.class, StorageProperties.class})
public class TourplannerApplication {

	static void main(String[] args) {
		SpringApplication.run(TourplannerApplication.class, args);
	}

}
