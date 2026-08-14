package Projeto.Gerenciador_Eventos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroInscricao;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharInscricao;
import Projeto.Gerenciador_Eventos.dto.DadosListarInscricao;
import Projeto.Gerenciador_Eventos.entity.Evento;
import Projeto.Gerenciador_Eventos.entity.Inscricao;
import Projeto.Gerenciador_Eventos.entity.Usuario;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;
import Projeto.Gerenciador_Eventos.repository.EventoRepository;
import Projeto.Gerenciador_Eventos.repository.InscricaoRepository;
import Projeto.Gerenciador_Eventos.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class InscricaoService {
	
	@Autowired
	private InscricaoRepository inscricaoRepository;
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public DadosDetalharInscricao cadastrarInscricao(DadosCadastroInscricao dados) {
		Usuario participante = usuarioLogado();
		Evento evento = eventoRepository.getReferenceById(dados.idEvento());
		
		boolean jaInscrito = inscricaoRepository.existsByEventoAndParticipanteAndStatusGeral(evento, participante, StatusGeral.ATIVO);
		if (jaInscrito) {
			throw new IllegalStateException("Você já possui uma inscrição ativa neste evento.");
		}
		
		if (evento.getVagasDisponiveisEvento() <= 0) {
			throw new IllegalStateException("Não há vagas disponíveis para este evento.");
		}
		
		Inscricao inscricao = new Inscricao();
		inscricao.setDataInscricao(dados.dataInscricao());
		inscricao.setEvento(evento);
		inscricao.setParticipante(participante);
		inscricao.setStatusGeral(StatusGeral.ATIVO);
		
		inscricaoRepository.save(inscricao);
		
		evento.setVagasDisponiveisEvento(evento.getVagasDisponiveisEvento() - 1);
		
		return new DadosDetalharInscricao(inscricao);
	}
	
	@Transactional
	public DadosDetalharInscricao ativarInscricao(Long id) {
		Inscricao inscricao = inscricaoRepository.getReferenceById(id);
		validarPosse(inscricao);
		
		if (inscricao.getStatusGeral() == StatusGeral.INATIVO) {
			Evento evento = inscricao.getEvento();
			if (evento.getVagasDisponiveisEvento() <= 0) {
				throw new IllegalStateException("Não há vagas disponíveis para reativar esta inscrição.");
			}
			evento.setVagasDisponiveisEvento(evento.getVagasDisponiveisEvento() - 1);
		}
		
		inscricao.setStatusGeral(StatusGeral.ATIVO);
		
		return new DadosDetalharInscricao(inscricao);
	}
	
	@Transactional
	public DadosDetalharInscricao inativarInscricao(Long id) {
		Inscricao inscricao = inscricaoRepository.getReferenceById(id);
		validarPosse(inscricao);
		
		if (inscricao.getStatusGeral() == StatusGeral.ATIVO) {
			Evento evento = inscricao.getEvento();
			evento.setVagasDisponiveisEvento(evento.getVagasDisponiveisEvento() + 1);
		}
		
		inscricao.setStatusGeral(StatusGeral.INATIVO);
		
		return new DadosDetalharInscricao(inscricao);
	}
	
	public DadosDetalharInscricao detalharInscricao(Long id) {
		Inscricao inscricao = inscricaoRepository.getReferenceById(id);
		
		return new DadosDetalharInscricao(inscricao);
	}
	
	public List<DadosDetalharInscricao> listarInscricoes() {
		List<Inscricao> inscricoes = inscricaoRepository.findAll();
		List<DadosDetalharInscricao> detalharDTOs = new ArrayList<>();
		
		for (Inscricao inscricao : inscricoes) {
			detalharDTOs.add(new DadosDetalharInscricao(inscricao));
		}
		
		return detalharDTOs;
	}
	
	public List<DadosDetalharInscricao> listarInscricoesComParametros(DadosListarInscricao parametros) {
		Evento evento = (parametros.idEvento() != null) ? 
	            eventoRepository.getReferenceById(parametros.idEvento()) : null;
	            
	    Usuario participante = (parametros.idParticipante() != null) ? 
	            usuarioRepository.getReferenceById(parametros.idParticipante()) : null;
	    
		List<Inscricao> inscricoes = inscricaoRepository.buscarComFiltrosDinamicos(
				evento, 
				participante, 
				parametros.dataInscricao(), 
				parametros.statusGeral());
		
		List<DadosDetalharInscricao> detalharDTOs = new ArrayList<>();
		
		for (Inscricao inscricao : inscricoes) {
			detalharDTOs.add(new DadosDetalharInscricao(inscricao));
		}
		
		return detalharDTOs;
	}
	
	// O próprio participante (dono da inscrição), o organizador do evento em que essa
	// inscrição foi feita, ou um ADMIN podem ativar/cancelar a inscrição.
	private void validarPosse(Inscricao inscricao) {
		Usuario usuarioLogado = usuarioLogado();
		boolean ehParticipante = inscricao.getParticipante().getIdUsuario().equals(usuarioLogado.getIdUsuario());
		boolean ehOrganizadorDoEvento = inscricao.getEvento().getOrganizador().getIdUsuario().equals(usuarioLogado.getIdUsuario());
		
		if (!ehParticipante && !ehOrganizadorDoEvento && !usuarioLogado.isAdmin()) {
			throw new AccessDeniedException("Você não tem permissão para alterar esta inscrição.");
		}
	}
	
	private Usuario usuarioLogado() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
