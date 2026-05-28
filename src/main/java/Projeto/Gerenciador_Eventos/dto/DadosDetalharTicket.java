package Projeto.Gerenciador_Eventos.dto;

import Projeto.Gerenciador_Eventos.entity.Inscricao;
import Projeto.Gerenciador_Eventos.entity.Ticket;

public record DadosDetalharTicket(

		    Long idTicket,
		    Inscricao inscricao,
		    String codigoHashTicket) {
	
	public DadosDetalharTicket(Ticket ticket) {
		this(ticket.getIdTicket(), ticket.getInscricao(), ticket.getCodigoHashTicket());
	}
}
