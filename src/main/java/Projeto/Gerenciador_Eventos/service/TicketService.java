package Projeto.Gerenciador_Eventos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroTicket;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharTicket;
import Projeto.Gerenciador_Eventos.dto.DadosListarTicket;
import Projeto.Gerenciador_Eventos.entity.Inscricao;
import Projeto.Gerenciador_Eventos.entity.Ticket;
import Projeto.Gerenciador_Eventos.entity.Usuario;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;
import Projeto.Gerenciador_Eventos.repository.InscricaoRepository;
import Projeto.Gerenciador_Eventos.repository.TicketRepository;
import Projeto.Gerenciador_Eventos.util.QrCodeGenerator;
import jakarta.transaction.Transactional;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private InscricaoRepository inscricaoRepository;
	
	@Transactional
	public DadosDetalharTicket cadastrarTicket(DadosCadastroTicket dados) {
		Inscricao inscricao = inscricaoRepository.getReferenceById(dados.idInscricao());
		
		Usuario usuarioLogado = usuarioLogado();
		boolean ehDono = inscricao.getParticipante().getIdUsuario().equals(usuarioLogado.getIdUsuario());
		if (!ehDono && !usuarioLogado.isAdmin()) {
			throw new AccessDeniedException("Você só pode emitir ticket para a sua própria inscrição.");
		}
		
		if (inscricao.getStatusGeral() != StatusGeral.ATIVO) {
			throw new IllegalStateException("Não é possível emitir ticket para uma inscrição inativa.");
		}
		
		Ticket ticket = new Ticket();
		ticket.setCodigoHashTicket(UUID.randomUUID().toString());
		ticket.setInscricao(inscricao);
		ticket.setStatusGeral(StatusGeral.ATIVO);
		
		ticketRepository.save(ticket);
		
		return new DadosDetalharTicket(ticket);
	}
	
	// Gera a imagem PNG do QR code do ticket. Só o dono do ticket (o próprio participante)
	// ou um ADMIN pode ver o QR code — o organizador do evento não precisa vê-lo, ele
	// só precisa escaneá-lo (ver CheckInService.escanearTicket).
	public byte[] gerarQrCode(Long id) {
		Ticket ticket = ticketRepository.getReferenceById(id);
		
		Usuario usuarioLogado = usuarioLogado();
		boolean ehDono = ticket.getInscricao().getParticipante().getIdUsuario().equals(usuarioLogado.getIdUsuario());
		if (!ehDono && !usuarioLogado.isAdmin()) {
			throw new AccessDeniedException("Você só pode ver o QR code do seu próprio ticket.");
		}
		
		return QrCodeGenerator.gerarPng(ticket.getCodigoHashTicket());
	}
	
	@Transactional
	public DadosDetalharTicket ativarTicket(Long id) {
		Ticket ticket = ticketRepository.getReferenceById(id);
		validarPosse(ticket);
		
		ticket.setStatusGeral(StatusGeral.ATIVO);
		
		return new DadosDetalharTicket(ticket);
	}
	
	@Transactional
	public DadosDetalharTicket inativarTicket(Long id) {
		Ticket ticket = ticketRepository.getReferenceById(id);
		validarPosse(ticket);
		
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
	
	/*
	 * Só o dono do ticket (o participante da inscrição), o organizador do evento ou um
	 * ADMIN podem ativar/inativar um ticket. Sem isso, qualquer usuário autenticado
	 * conseguia inativar o ticket de outra pessoa e impedir o check-in dela na entrada
	 * (escanearTicket recusa ticket inativo).
	 */
	private void validarPosse(Ticket ticket) {
		Usuario usuarioLogado = usuarioLogado();
		boolean ehDono = ticket.getInscricao().getParticipante().getIdUsuario().equals(usuarioLogado.getIdUsuario());
		boolean ehOrganizadorDoEvento = ticket.getInscricao().getEvento().getOrganizador().getIdUsuario().equals(usuarioLogado.getIdUsuario());
		
		if (!ehDono && !ehOrganizadorDoEvento && !usuarioLogado.isAdmin()) {
			throw new AccessDeniedException("Você não tem permissão para alterar este ticket.");
		}
	}
	
	private Usuario usuarioLogado() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}