package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record DadosCadastroTicket(
		
		@NotNull
		Integer idInscricao,
		
		@NotBlank
		String codigoHashTicket,
		
		@PastOrPresent
		LocalDate dataCheckInTicket) {

}
