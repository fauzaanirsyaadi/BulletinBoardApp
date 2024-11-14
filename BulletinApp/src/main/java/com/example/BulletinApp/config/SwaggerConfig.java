package com.example.BulletinApp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bulletinBoardOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Bulletin Board API")
                        .description("Spring Boot REST API for Bulletin Board Application")
                        .version("1.0.0"));
    }
}