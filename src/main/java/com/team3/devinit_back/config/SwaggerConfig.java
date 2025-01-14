package com.team3.devinit_back.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                        .description("테스트용 JWT 토큰을 입력하세요. 형식: Bearer {token}")
                ))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
            .title("Devinit Springdoc")
            .description("Springdoc을 사용한 Devinit Swagger UI")
            .version("1.0.0");
    }
}