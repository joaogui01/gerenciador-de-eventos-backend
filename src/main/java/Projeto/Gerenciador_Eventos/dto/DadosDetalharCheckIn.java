package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDateTime;

import Projeto.Gerenciador_Eventos.entity.CheckIn;
import Projeto.Gerenciador_Eventos.entity.Ticket;

public record DadosDetalharCheckIn(

	    Long idCheckIn,
	    Ticket ticket,
	    LocalDateTime dataCheckIn) {
	
	public DadosDetalharCheckIn(CheckIn checkIn) {
		this(checkIn.getIdCheckIn(), checkIn.getTicket(), checkIn.getDataCheckIn());
	}
}
