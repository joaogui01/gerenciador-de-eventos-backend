package Projeto.Gerenciador_Eventos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosCadastroEvento(	
		@NotBlank
		String nomeEvento,
		
		@NotBlank
		String descricaoEvento,
		
		@FutureOrPresent
		LocalDateTime dataHorarioEvento,
		
		@NotBlank
		String localEvento,
		
		@NotNull
		Integer vagasTotaisEvento,
		
		@NotNull
		Integer vagasDisponiveisEvento,
		
		@Positive
		BigDecimal precoEvento,
		
		@NotNull
		Integer idStatus) {

}
