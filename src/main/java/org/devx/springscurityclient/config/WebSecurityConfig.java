package org.devx.springscurityclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration rather than using this we can use below annotation because we are working with spring security
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URLS = {
            "/","/login","/hello","/register",
            "/verifyRegistration*","/resendVerifyToken*",
            "/resetPassword*","/savePassword*","/changePassword*",};

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {

        http
//                .csrf(csrf->csrf.disable())
//                .cors(cors->cors.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth)-> auth
                        .requestMatchers(WHITE_LIST_URLS).permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();

//        http.authorizeRequests().requestMatchers(WHITE_LIST_URLS).permitAll()
//                .and().csrf(csrf -> csrf
//                .ignoringRequestMatchers(WHITE_LIST_URLS))
//                .cors(withDefaults());
//
//        return http.build();


    }
}
