package br.uff.balcao_uff.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("APIs Projeto "
                		+ "Balcão UFF")
                .version("1.0.0")
                .description("Documentação das APIs "
                		+ "Projeto Balcão UFF para "
                		+ "disciplina de Gerência de "
                		+ "Projetos e Manutenção de Software"));
    }
}
