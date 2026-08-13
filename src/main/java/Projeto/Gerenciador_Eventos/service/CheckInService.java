package Projeto.Gerenciador_Eventos.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroCheckIn;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharCheckIn;
import Projeto.Gerenciador_Eventos.dto.DadosEscanearTicket;
import Projeto.Gerenciador_Eventos.dto.DadosListarCheckIn;
import Projeto.Gerenciador_Eventos.entity.CheckIn;
import Projeto.Gerenciador_Eventos.entity.Ticket;
import Projeto.Gerenciador_Eventos.entity.Usuario;
import Projeto.Gerenciador_Eventos.entity.enums.StatusCheckIn;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;
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
	
	// Só o organizador do evento (é ele quem escaneia o QR code na entrada) ou um ADMIN
	// pode confirmar o check-in.
	@Transactional
	public DadosDetalharCheckIn realizarCheckIn(Long id) {
		CheckIn checkIn = checkInRepository.getReferenceById(id);
		
		Usuario organizadorDoEvento = checkIn.getTicket().getInscricao().getEvento().getOrganizador();
		Usuario usuarioLogado = usuarioLogado();
		boolean ehOrganizador = organizadorDoEvento.getIdUsuario().equals(usuarioLogado.getIdUsuario());
		if (!ehOrganizador && !usuarioLogado.isAdmin()) {
			throw new AccessDeniedException("Só o organizador do evento pode confirmar o check-in.");
		}
		
		checkIn.setStatusCheckIn(StatusCheckIn.REALIZADO);
		
		return new DadosDetalharCheckIn(checkIn);
	}
	
	/*
	 * Fluxo pensado para o app do organizador: ele aponta a câmera do celular pro QR code
	 * do participante, o app lê o texto (o codigoHashTicket) e manda pra cá. Aqui a gente
	 * acha o ticket pelo código, confere se quem está chamando é o organizador do evento,
	 * e confirma o check-in — cria o registro se ainda não existir, ou atualiza se já existir.
	 */
	@Transactional
	public DadosDetalharCheckIn escanearTicket(DadosEscanearTicket dados) {
		Ticket ticket = ticketRepository.findByCodigoHashTicket(dados.codigoHashTicket());
		
		if (ticket == null) {
			throw new IllegalStateException("Ticket não encontrado. Confira o QR code.");
		}
		
		if (ticket.getStatusGeral() != StatusGeral.ATIVO) {
			throw new IllegalStateException("Este ticket está inativo.");
		}
		
		Usuario organizadorDoEvento = ticket.getInscricao().getEvento().getOrganizador();
		Usuario usuarioLogado = usuarioLogado();
		boolean ehOrganizador = organizadorDoEvento.getIdUsuario().equals(usuarioLogado.getIdUsuario());
		if (!ehOrganizador && !usuarioLogado.isAdmin()) {
			throw new AccessDeniedException("Só o organizador do evento pode confirmar o check-in.");
		}
		
		CheckIn checkIn = checkInRepository.findByTicket(ticket);
		
		if (checkIn != null && checkIn.getStatusCheckIn() == StatusCheckIn.REALIZADO) {
			throw new IllegalStateException("Check-in já realizado para este ticket.");
		}
		
		if (checkIn == null) {
			checkIn = new CheckIn();
			checkIn.setTicket(ticket);
		}
		
		checkIn.setDataCheckIn(LocalDateTime.now());
		checkIn.setStatusCheckIn(StatusCheckIn.REALIZADO);
		
		checkInRepository.save(checkIn);
		
		return new DadosDetalharCheckIn(checkIn);
	}
	
	public DadosDetalharCheckIn detalharCheckIn(Long id) {
		CheckIn checkIn = checkInRepository.getReferenceById(id);
		
		return new DadosDetalharCheckIn(checkIn);
	}
	
	public List<DadosDetalharCheckIn> listarCheckIns() {
		List<CheckIn> checkins = checkInRepository.findAll();
		List<DadosDetalharCheckIn> detalharDTOs = new ArrayList<>();
		
		for (CheckIn checkin : checkins) {
			detalharDTOs.add(new DadosDetalharCheckIn(checkin));
		}
		
		return detalharDTOs;
	}
	
	public List<DadosDetalharCheckIn> listarCheckInsComParametros(DadosListarCheckIn parametros) {
		Ticket ticket = (parametros.idTicket() != null) ? 
	            ticketRepository.getReferenceById(parametros.idTicket()) : null;
		
		List<CheckIn> checkins = checkInRepository.buscarComFiltrosDinamicos(
				ticket, 
				parametros.dataCheckIn(), 
				parametros.statusCheckIn());
		
		List<DadosDetalharCheckIn> detalharDTOs = new ArrayList<>();
		
		for (CheckIn checkin : checkins) {
			detalharDTOs.add(new DadosDetalharCheckIn(checkin));
		}
		
		return detalharDTOs;
	}
	
	private Usuario usuarioLogado() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}