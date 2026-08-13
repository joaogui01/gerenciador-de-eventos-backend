package Projeto.Gerenciador_Eventos.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroUsuario(

		@NotBlank
		String nome,

		@NotBlank
		String login,

		@NotBlank
		String senha,

		@NotBlank
		String cpf,

		String telefone) {

}
