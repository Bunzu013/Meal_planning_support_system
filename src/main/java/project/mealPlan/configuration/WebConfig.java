 package project.mealPlan.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Tutaj możesz określić, które ścieżki będą dostępne
                .allowedOrigins("http://localhost:3000") // Określ dozwolone domeny
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // Określ dozwolone metody HTTP
    }
}
