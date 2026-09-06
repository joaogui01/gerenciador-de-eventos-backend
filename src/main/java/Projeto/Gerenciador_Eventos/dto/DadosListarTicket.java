package Projeto.Gerenciador_Eventos.dto;

import Projeto.Gerenciador_Eventos.entity.Ticket;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;

public record DadosListarTicket(
		
	    Long idInscricao,
	    String codigoHashTicket,
	    StatusGeral statusGeral) {

	public DadosListarTicket(Ticket ticket) {
		this(
				ticket.getInscricao().getIdInscricao(), 
				ticket.getCodigoHashTicket(),
				ticket.getStatusGeral());
	}
}