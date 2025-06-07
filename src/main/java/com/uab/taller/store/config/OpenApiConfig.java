package com.uab.taller.store.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info().title("store service")
                .description("es una aplicacion de store")
                .version("1.0")
                .license(new License().name("apache").url("https://www.apache.org/licenses/LICENSE-2.0"))
                .version("1.0")
                ;
    }
}
