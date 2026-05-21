package com.listacompras.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lista de Compras API")
                        .description("API REST para gerenciamento de listas de compras, mercados e itens. " +
                                "Permite criar e gerenciar mercados, listas de compras associadas a mercados " +
                                "e itens dentro de cada lista.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pedro Vaz & Joao Victor")
                                .email("pedrovazferreira10@gmail.com")));
    }
}
