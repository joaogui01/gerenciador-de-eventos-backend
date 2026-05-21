package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record DadosCadastroInscricao(
		
		@NotNull
		Long idEvento,
		
		@NotNull
		Long idParticipante,
		
		@PastOrPresent
		LocalDate dataInscricao) {

}
