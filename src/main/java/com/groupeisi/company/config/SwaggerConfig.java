package com.groupeisi.company.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${keycloak.auth-server-url:http://localhost:8080}")
    private String keycloakUrl;

    @Value("${keycloak.realm:achat-en-ligne}")
    private String realm;

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "Keycloak";

        String tokenUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        String authUrl  = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/auth";

        return new OpenAPI()
                .info(new Info()
                        .title("Achat En Ligne - API")
                        .description("Microservice de gestion des achats en ligne")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Groupe ISI")
                                .email("contact@groupeisi.com")
                                .url("https://www.groupeisi.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .flows(new OAuthFlows()
                                                .password(new OAuthFlow()
                                                        .tokenUrl(tokenUrl)
                                                        .authorizationUrl(authUrl))
                                                .authorizationCode(new OAuthFlow()
                                                        .tokenUrl(tokenUrl)
                                                        .authorizationUrl(authUrl)))));
    }
}
