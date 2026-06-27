package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDateTime;

import Projeto.Gerenciador_Eventos.entity.CheckIn;
import Projeto.Gerenciador_Eventos.entity.enums.StatusCheckIn;

public record DadosListarCheckIn(	    
		
	    Long idTicket,
	    LocalDateTime dataCheckIn,
	    StatusCheckIn statusCheckIn) {
	
	public DadosListarCheckIn(CheckIn checkIn) {
		this(checkIn.getTicket().getIdTicket(), checkIn.getDataCheckIn(), checkIn.getStatusCheckIn());
	}
}