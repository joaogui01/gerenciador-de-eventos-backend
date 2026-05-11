package Projeto.Gerenciador_Eventos.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroTicket(
		
		@NotNull
		Integer idInscricao,
		
		@NotBlank
		String codigoHashTicket) {

}
