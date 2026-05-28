package Projeto.Gerenciador_Eventos.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroTicket(
		
		@NotNull
		Long idInscricao,
		
		@NotBlank
		String codigoHashTicket) {

}
