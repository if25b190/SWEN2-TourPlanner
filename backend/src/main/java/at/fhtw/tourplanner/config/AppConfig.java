package at.fhtw.tourplanner.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tourplanner")
public class AppConfig {
    private String domain = "";
    private String rememberMeKey = "";
}
