package api.saylix.uz.config;


import api.saylix.uz.enums.AppLanguage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@Configuration
public class AppConfig {

    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/message");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(new Locale("uz"));

        return messageSource;
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

