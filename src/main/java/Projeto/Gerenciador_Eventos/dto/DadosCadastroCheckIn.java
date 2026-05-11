package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record DadosCadastroCheckIn(
		
		@PastOrPresent
		LocalDate dataCheckInTicket,
		
		@NotNull
		Integer idTicket,
		
		@NotNull
		Integer idStatusCheckIn) {

}
