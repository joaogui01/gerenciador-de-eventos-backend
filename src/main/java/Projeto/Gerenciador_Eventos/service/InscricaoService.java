package Projeto.Gerenciador_Eventos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroInscricao;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharInscricao;
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
		Inscricao inscricao = new Inscricao();
		inscricao.setDataInscricao(dados.dataInscricao());
		
		Evento evento = eventoRepository.getReferenceById(dados.idEvento());
		inscricao.setEvento(evento);
		
		Participante participante = participanteRepository.getReferenceById(dados.idParticipante());
		inscricao.setParticipante(participante);
		
		inscricao.setStatusGeral(StatusGeral.ATIVO);
		
		inscricaoRepository.save(inscricao);
		
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
}
