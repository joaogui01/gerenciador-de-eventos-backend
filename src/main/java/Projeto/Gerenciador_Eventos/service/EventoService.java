package Projeto.Gerenciador_Eventos.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosAtualizarEvento;
import Projeto.Gerenciador_Eventos.dto.DadosCadastroEvento;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharEvento;
import Projeto.Gerenciador_Eventos.dto.DadosListarEvento;
import Projeto.Gerenciador_Eventos.entity.Evento;
import Projeto.Gerenciador_Eventos.entity.Usuario;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;
import Projeto.Gerenciador_Eventos.repository.EventoRepository;
import jakarta.transaction.Transactional;

@Service
public class EventoService {
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Transactional 
	public DadosDetalharEvento cadastrarEvento(DadosCadastroEvento dados) {
		Usuario usuarioLogado = usuarioLogado();
		
		Evento evento = new Evento();
		evento.setNomeEvento(dados.nomeEvento());
		evento.setDescricaoEvento(dados.descricaoEvento());
		evento.setDataEvento(dados.dataEvento());
		evento.setLocalEvento(dados.localEvento());
		evento.setVagasTotaisEvento(dados.vagasTotaisEvento());
		evento.setVagasDisponiveisEvento(dados.vagasTotaisEvento());
		evento.setPrecoEvento(dados.precoEvento());
		evento.setStatusGeral(StatusGeral.ATIVO);
		evento.setOrganizador(usuarioLogado);
		
		eventoRepository.save(evento);
		
		return new DadosDetalharEvento(evento);
	}
	
	@Transactional
	public DadosDetalharEvento atualizarEvento(DadosAtualizarEvento dados) {
		Evento evento = eventoRepository.getReferenceById(dados.idEvento());
		validarPosse(evento);
		
		evento.atualizarInformações(dados);
		
		return new DadosDetalharEvento(evento);
	}
	
	@Transactional
	public DadosDetalharEvento ativarEvento(Long id) {
		Evento evento = eventoRepository.getReferenceById(id);
		validarPosse(evento);
		
		evento.setStatusGeral(StatusGeral.ATIVO);
		
		return new DadosDetalharEvento(evento);
	}
	
	@Transactional
	public DadosDetalharEvento inativarEvento(Long id) {
		Evento evento = eventoRepository.getReferenceById(id);
		validarPosse(evento);
		
		evento.setStatusGeral(StatusGeral.INATIVO);
		
		return new DadosDetalharEvento(evento);
	}
	
	public DadosDetalharEvento detalharEvento(Long id) {
		Evento evento = eventoRepository.getReferenceById(id);
		
		return new DadosDetalharEvento(evento);
	}
	
	public List<DadosDetalharEvento> listarEventos() {
		List<Evento> eventos = eventoRepository.findAll();
		List<DadosDetalharEvento> detalharDTOs = new ArrayList<>();
		
		for (Evento evento : eventos) {
			detalharDTOs.add(new DadosDetalharEvento(evento));
		}
		
		return detalharDTOs;
	}
	
	public List<DadosDetalharEvento> listarEventosComParametros(DadosListarEvento parametros) {
		List<Evento> eventos = eventoRepository.buscarComFiltrosDinamicos(
				parametros.nomeEvento(), 
				parametros.descricaoEvento(), 
				parametros.dataEvento(), 
				parametros.localEvento(), 
				parametros.vagasTotaisEvento(), 
				parametros.vagasDisponiveisEvento(), 
				parametros.precoEvento(), 
				parametros.statusGeral());
		
		List<DadosDetalharEvento> detalharDTOs = new ArrayList<>();
		
		for (Evento evento : eventos) {
			detalharDTOs.add(new DadosDetalharEvento(evento));
		}
		
		return detalharDTOs;
	}
	
	// Só o organizador do evento (dono) ou um ADMIN pode alterar/ativar/inativar o evento.
	private void validarPosse(Evento evento) {
		Usuario usuarioLogado = usuarioLogado();
		boolean ehDono = evento.getOrganizador().getIdUsuario().equals(usuarioLogado.getIdUsuario());
		
		if (!ehDono && !usuarioLogado.isAdmin()) {
			throw new AccessDeniedException("Você não tem permissão para alterar este evento.");
		}
	}
	
	private Usuario usuarioLogado() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
