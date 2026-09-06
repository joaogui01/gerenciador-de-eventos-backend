package Projeto.Gerenciador_Eventos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosCadastroEvento(	
		@NotBlank
		String nomeEvento,
		
		@NotBlank
		String descricaoEvento,
		
		@NotNull
		@FutureOrPresent
		LocalDate dataEvento,
		
		@NotBlank
		String localEvento,
		
		@NotNull
		Integer vagasTotaisEvento,
		
		// Ignorado no cadastro: quem cria o evento não escolhe quantas vagas já estão
		// livres — o EventoService sempre inicia vagasDisponiveis = vagasTotais. O campo
		// continua aqui só pra não quebrar clientes que já mandavam ele.
		Integer vagasDisponiveisEvento,
		
		@Positive
		BigDecimal precoEvento) {

}
