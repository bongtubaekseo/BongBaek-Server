package org.appjam.bongbaek.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

// @Profile("dev")
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Bongbaek Server API",
                version = "1.0",
                description = "AT SOPT 봉투백서 서버 API SWAGGER 입니다."
        )
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local Server");

        Server devServer = new Server()
                .url("https://dev.bongbaek.com")
                .description("Dev Server");

        return new OpenAPI().servers(List.of(localServer, devServer));
    }
}
