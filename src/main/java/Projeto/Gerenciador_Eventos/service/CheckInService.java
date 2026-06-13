package Projeto.Gerenciador_Eventos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroCheckIn;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharCheckIn;
import Projeto.Gerenciador_Eventos.entity.CheckIn;
import Projeto.Gerenciador_Eventos.entity.Ticket;
import Projeto.Gerenciador_Eventos.entity.enums.StatusCheckIn;
import Projeto.Gerenciador_Eventos.repository.CheckInRepository;
import Projeto.Gerenciador_Eventos.repository.TicketRepository;
import jakarta.transaction.Transactional;

@Service
public class CheckInService {
	
	@Autowired
	private CheckInRepository checkInRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Transactional
	public DadosDetalharCheckIn cadastrarCheckIn(DadosCadastroCheckIn dados) {
		CheckIn checkIn = new CheckIn();
		checkIn.setDataCheckIn(dados.dataCheckInTicket());
		
		Ticket ticket = ticketRepository.getReferenceById(dados.idTicket());
		checkIn.setTicket(ticket);
		
		checkIn.setStatusCheckIn(StatusCheckIn.NAO_REALIZADO);
		
		checkInRepository.save(checkIn);
		
		return new DadosDetalharCheckIn(checkIn);
	}
	
	@Transactional
	public DadosDetalharCheckIn realizarCheckIn(Long id) {
		CheckIn checkIn = checkInRepository.getReferenceById(id);
		checkIn.setStatusCheckIn(StatusCheckIn.REALIZADO);;
		
		return new DadosDetalharCheckIn(checkIn);
	}
}
