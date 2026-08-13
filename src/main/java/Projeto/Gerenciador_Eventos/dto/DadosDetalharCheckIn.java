package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDateTime;

import Projeto.Gerenciador_Eventos.entity.CheckIn;
import Projeto.Gerenciador_Eventos.entity.enums.StatusCheckIn;

public record DadosDetalharCheckIn(

	    Long idCheckIn,
	    Long idTicket,
	    LocalDateTime dataCheckIn,
	    StatusCheckIn statusCheckIn) {
	
	public DadosDetalharCheckIn(CheckIn checkIn) {
		this(
				checkIn.getIdCheckIn(), 
				checkIn.getTicket().getIdTicket(), 
				checkIn.getDataCheckIn(),
				checkIn.getStatusCheckIn()
		);
	}
}
