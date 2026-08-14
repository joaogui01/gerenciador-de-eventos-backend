package Projeto.Gerenciador_Eventos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/*
 * Configuração da documentação da API (Swagger/OpenAPI).
 * Com a aplicação rodando, a documentação interativa fica em /swagger-ui.html
 * e o JSON da especificação em /v3/api-docs.
 */
@Configuration
public class OpenApiConfig {

	private static final String NOME_ESQUEMA_SEGURANCA = "bearerAuth";

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
				.info(informacoesDaApi())
				.addSecurityItem(new SecurityRequirement().addList(NOME_ESQUEMA_SEGURANCA))
				.components(new Components().addSecuritySchemes(NOME_ESQUEMA_SEGURANCA, esquemaDeAutenticacao()));
	}

	private Info informacoesDaApi() {
		return new Info()
				.title("Gerenciador de Eventos API")
				.description("API REST para gerenciamento de eventos, inscrições, tickets e check-ins. "
						+ "A maioria dos endpoints exige um token JWT: faça login em POST /login, "
						+ "copie o token retornado e cole no botão \"Authorize\" acima (sem o prefixo \"Bearer \"). "
						+ "As exceções públicas, que não precisam de token, são POST /login e POST /usuario/cadastrar.")
				.version("v1");
	}

	private SecurityScheme esquemaDeAutenticacao() {
		return new SecurityScheme()
				.name(NOME_ESQUEMA_SEGURANCA)
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT");
	}

}
