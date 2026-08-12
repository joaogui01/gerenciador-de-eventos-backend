package Projeto.Gerenciador_Eventos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroInscricao;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharInscricao;
import Projeto.Gerenciador_Eventos.dto.DadosListarInscricao;
import Projeto.Gerenciador_Eventos.entity.Evento;
import Projeto.Gerenciador_Eventos.entity.Inscricao;
import Projeto.Gerenciador_Eventos.entity.Participante;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;
import Projeto.Gerenciador_Eventos.repository.EventoRepository;
import Projeto.Gerenciador_Eventos.repository.InscricaoRepository;
import Projeto.Gerenciador_Eventos.repository.ParticipanteRepository;
import jakarta.transaction.Transactional;

@Service
public class InscricaoService {
	
	@Autowired
	private InscricaoRepository inscricaoRepository;
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Autowired
	private ParticipanteRepository participanteRepository;
	
	@Transactional
	public DadosDetalharInscricao cadastrarInscricao(DadosCadastroInscricao dados) {
		Evento evento = eventoRepository.getReferenceById(dados.idEvento());
		Participante participante = participanteRepository.getReferenceById(dados.idParticipante());
		
		boolean jaInscrito = inscricaoRepository.existsByEventoAndParticipanteAndStatusGeral(evento, participante, StatusGeral.ATIVO);
		if (jaInscrito) {
			throw new IllegalStateException("Este participante já possui uma inscrição ativa neste evento.");
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
		inscricao.setStatusGeral(StatusGeral.ATIVO);
		
		return new DadosDetalharInscricao(inscricao);
	}
	
	@Transactional
	public DadosDetalharInscricao inativarInscricao(Long id) {
		Inscricao inscricao = inscricaoRepository.getReferenceById(id);
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
	            
	    Participante participante = (parametros.idParticipante() != null) ? 
	            participanteRepository.getReferenceById(parametros.idParticipante()) : null;
	    
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
}