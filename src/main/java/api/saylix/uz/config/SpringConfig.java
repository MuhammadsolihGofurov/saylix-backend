package api.saylix.uz.config;

import api.saylix.uz.exps.CustomSecurityExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public static final String[] AUTH_WHITELIST = {
            "/api/v1/user/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/api/v1/user/check/status",
            "/error"
    };


    @Bean
    public AuthenticationProvider authenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder) {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomSecurityExceptionHandler securityHandler) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(securityHandler)
                        .accessDeniedHandler(securityHandler)
                );


        return http.build();
    }

}

