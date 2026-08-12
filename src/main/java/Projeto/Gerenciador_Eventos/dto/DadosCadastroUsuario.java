package Projeto.Gerenciador_Eventos.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroUsuario(

		@NotBlank
		String login,

		@NotBlank
		String senha) {

}