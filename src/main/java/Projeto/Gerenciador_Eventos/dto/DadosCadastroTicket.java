package Projeto.Gerenciador_Eventos.dto;

import jakarta.validation.constraints.NotNull;

/*
 * Não tem codigoHashTicket aqui de propósito: o código único do ticket (usado no QR code)
 * é gerado pelo próprio servidor no TicketService, nunca informado pelo cliente.
 */
public record DadosCadastroTicket(
		
		@NotNull
		Long idInscricao) {

}
