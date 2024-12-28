package com.app.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:swagger/swagger.properties")
@Configuration
@SecurityScheme(
    name = AppConstants.SECURITY_CONTEXT_PARAM,
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {

  @Bean
  public OpenAPI shopOpenAPI() {

    return new OpenAPI()
        .info(
            new Info()
                .title("Mithra App- ")
                .description("API documentation for the subscription model services")
                .version("v1")
                .contact(
                    new Contact()
                        .name("Swamy Kunta")
                        .url("https://github.com/s713278/SubscriptionApp/tree/master")
                        .email("swamy.kunta@gmail.com"))
                .license(new License().name("License").url("/")))
        .externalDocs(
            new ExternalDocumentation()
                .description("MithraDirect Application")
                .url("http://localhost:8080/api/swagger-ui/index.html"));
    // .servers(List.of(localServer,devServer));

  }
}
