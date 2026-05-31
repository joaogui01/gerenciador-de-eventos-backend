package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record DadosCadastroCheckIn(
		
		@PastOrPresent
		LocalDateTime dataCheckInTicket,
		
		@NotNull
		Long idTicket) {

}
