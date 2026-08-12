package Projeto.Gerenciador_Eventos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroTicket;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharTicket;
import Projeto.Gerenciador_Eventos.dto.DadosListarTicket;
import Projeto.Gerenciador_Eventos.entity.Inscricao;
import Projeto.Gerenciador_Eventos.entity.Ticket;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;
import Projeto.Gerenciador_Eventos.repository.InscricaoRepository;
import Projeto.Gerenciador_Eventos.repository.TicketRepository;
import jakarta.transaction.Transactional;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private InscricaoRepository inscricaoRepository;
	
	@Transactional
	public DadosDetalharTicket cadastrarTicket(DadosCadastroTicket dados) {
		Ticket ticket = new Ticket();
		ticket.setCodigoHashTicket(dados.codigoHashTicket());
		
		Inscricao inscricao = inscricaoRepository.getReferenceById(dados.idInscricao());
		ticket.setInscricao(inscricao);
		
		ticket.setStatusGeral(StatusGeral.ATIVO);
		
		ticketRepository.save(ticket);
		
		return new DadosDetalharTicket(ticket);
	}
	
	@Transactional
	public DadosDetalharTicket ativarTicket(Long id) {
		Ticket ticket = ticketRepository.getReferenceById(id);
		ticket.setStatusGeral(StatusGeral.ATIVO);
		
		return new DadosDetalharTicket(ticket);
	}
	
	@Transactional
	public DadosDetalharTicket inativarTicket(Long id) {
		Ticket ticket = ticketRepository.getReferenceById(id);
		ticket.setStatusGeral(StatusGeral.INATIVO);
		
		return new DadosDetalharTicket(ticket);
	}
	
	public DadosDetalharTicket detalharTicket(Long id) {
		Ticket ticket = ticketRepository.getReferenceById(id);
		
		return new DadosDetalharTicket(ticket);
	}
	
	public List<DadosDetalharTicket> listarTickets() {
		List<Ticket> tickets = ticketRepository.findAll();
		List<DadosDetalharTicket> detalharDTOs = new ArrayList<>();
		
		for (Ticket ticket : tickets) {
			detalharDTOs.add(new DadosDetalharTicket(ticket));
		}
		
		return detalharDTOs;
	}
	
	public List<DadosDetalharTicket> listarTicketsComParametros(DadosListarTicket parametros) {
		Inscricao inscricao = (parametros.idInscricao() != null) ? 
	            inscricaoRepository.getReferenceById(parametros.idInscricao()) : null;
		
		List<Ticket> tickets = ticketRepository.buscarComFiltrosDinamicos(
				inscricao, 
				parametros.codigoHashTicket(), 
				parametros.statusGeral());
		
		List<DadosDetalharTicket> detalharDTOs = new ArrayList<>();
		
		for (Ticket ticket : tickets) {
			detalharDTOs.add(new DadosDetalharTicket(ticket));
		}
		
		return detalharDTOs;
	}
}