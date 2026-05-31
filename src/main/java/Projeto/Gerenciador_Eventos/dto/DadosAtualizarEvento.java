package Projeto.Gerenciador_Eventos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizarEvento(	
		
		@NotNull
		Long idEvento,

		String nomeEvento,
		String descricaoEvento,
		LocalDate dataEvento,
		String localEvento,
		Integer vagasTotaisEvento,
		Integer vagasDisponiveisEvento,
		BigDecimal precoEvento) {

}
