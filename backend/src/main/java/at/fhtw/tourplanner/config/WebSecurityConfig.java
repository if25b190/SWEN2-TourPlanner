package at.fhtw.tourplanner.config;

import at.fhtw.tourplanner.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class WebSecurityConfig {
    @Value("tourplanner.domain")
    private String domain;
    @Value("tourplanner.rememberMeKey")
    private String rememberMeKey;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                domain
        ));
        config.addAllowedHeader("*");
        config.setAllowedMethods(List.of("OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE"));
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/logout", config);
        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configureRest(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers(HttpMethod.OPTIONS)
                        .permitAll()
                        .requestMatchers("/h2-console/**")
                        .hasRole("ADMIN")
                        .requestMatchers(
                                "/api/v1/register"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(conf -> conf
                        .successHandler((req, res, auth) -> {
                            res.setStatus(200);
                            handleCors(req, res);
                        })
                        .failureHandler((req, res, exception) -> {
                            res.setStatus(400);
                            Optional.of(exception)
                                    .map(Throwable::getLocalizedMessage)
                                    .ifPresent(res.getWriter()::append);
                            handleCors(req, res);
                        })
                        .loginPage("/api/v1/login")
                        .permitAll()
                        .loginProcessingUrl("/api/v1/login")
                        .permitAll()
                )
                .logout(conf -> conf
                        .logoutUrl("/api/v1/logout")
                        .logoutSuccessHandler((req, res, auth) -> {
                            res.setStatus(200);
                            handleCors(req, res);
                        })
                        .deleteCookies("JSESSIONID")
                )
                .rememberMe(conf -> conf
                        .key(rememberMeKey)
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(5184000)
                        .useSecureCookie(true)
                )
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint((req, res, exception) -> {
                            res.setStatus(401);
                            Optional.of(exception)
                                    .map(Throwable::getLocalizedMessage)
                                    .ifPresent(res.getWriter()::append);
                            handleCors(req, res);
                        })
                )
                .build();
    }

    @Autowired
    public void configurePasswordEncoder(AuthenticationManagerBuilder builder) {
        builder.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    private void handleCors(HttpServletRequest request, HttpServletResponse response) {
        var origin = request.getHeader("origin");
        if (origin != null && (origin.contains("localhost") || origin.contains("127.0.0.1"))) {
            response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        } else {
            response.addHeader("Access-Control-Allow-Origin", domain);
        }
        response.addHeader("Access-Control-Allow-Credentials", "true");
    }
}
