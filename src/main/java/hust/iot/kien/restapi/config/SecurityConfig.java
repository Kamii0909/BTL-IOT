package hust.iot.kien.restapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {

    @Value("server.port")
    private int port;

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity bean) throws Exception {
        // @formatter:off
        bean.authorizeHttpRequests()
            .requestMatchers("/user/**")
                .hasRole("USER")
            .requestMatchers("/device/**")
                .hasRole("USER")
                .and()
            .formLogin()
                .permitAll()
                .and()
            .rememberMe()
                .alwaysRemember(true)
                .rememberMeCookieName("user_token")
                .and()
            .requiresChannel()
                .anyRequest()
                // IMPORTANT
                .requiresInsecure()
                .and()
            .portMapper()
                .http(80).mapsTo(port)
                .http(443).mapsTo(port)
                .http(8080).mapsTo(port);
        return bean.build();
        // @formatter:on
    }

}
