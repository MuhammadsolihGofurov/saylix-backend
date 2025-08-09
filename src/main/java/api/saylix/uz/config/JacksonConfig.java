package api.saylix.uz.config;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Java 8 vaqt tiplari uchun modul
        mapper.registerModule(new JavaTimeModule());
        // LocalDateTime ni timestamp o‘rniga stringda chiqarish
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Barcha qo‘shimcha modullarni avtomatik yuklash
        mapper.findAndRegisterModules();
        return mapper;
    }
}
