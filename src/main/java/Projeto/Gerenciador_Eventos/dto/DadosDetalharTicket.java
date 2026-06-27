package Projeto.Gerenciador_Eventos.dto;

import Projeto.Gerenciador_Eventos.entity.Ticket;

public record DadosDetalharTicket(

		    Long idTicket,
		    Long idInscricao,
		    String codigoHashTicket) {
	
	public DadosDetalharTicket(Ticket ticket) {
		this(ticket.getIdTicket(), ticket.getInscricao().getIdInscricao(), ticket.getCodigoHashTicket());
	}
}
