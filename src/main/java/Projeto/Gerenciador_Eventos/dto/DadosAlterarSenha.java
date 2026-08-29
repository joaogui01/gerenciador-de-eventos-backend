package Projeto.Gerenciador_Eventos.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosAlterarSenha(

		@NotBlank
		String senhaAtual,

		@NotBlank
		String novaSenha) {

}
