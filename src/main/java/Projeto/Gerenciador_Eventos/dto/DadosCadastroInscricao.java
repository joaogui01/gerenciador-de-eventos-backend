package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record DadosCadastroInscricao(
		
		@NotNull
		Integer idEvento,
		
		@NotNull
		Integer idParticipante,
		
		@PastOrPresent
		LocalDate dataInscricao) {

}
