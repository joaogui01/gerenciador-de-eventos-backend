package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

/*
 * Não tem idParticipante aqui de propósito: o participante da inscrição é sempre
 * o usuário autenticado que está fazendo a requisição (auto-inscrição), nunca
 * escolhido livremente pelo corpo da requisição.
 */
public record DadosCadastroInscricao(
		
		@NotNull
		Long idEvento,
		
		@PastOrPresent
		LocalDate dataInscricao) {

}
