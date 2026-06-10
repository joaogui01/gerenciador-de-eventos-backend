package Projeto.Gerenciador_Eventos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroParticipante;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharParticipante;
import Projeto.Gerenciador_Eventos.entity.Participante;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;
import Projeto.Gerenciador_Eventos.repository.ParticipanteRepository;
import jakarta.transaction.Transactional;

@Service
public class ParticipanteService {
	
	@Autowired
	private ParticipanteRepository participanteRepository;
	
	@Transactional
	public DadosDetalharParticipante cadastrarParticipante(DadosCadastroParticipante dados) {
		Participante participante = new Participante();
		participante.setCpfParticipante(dados.cpfParticipante());
		participante.setEmailParticipante(dados.emailParticipante());
		participante.setNomeParticipante(dados.nomeParticipante());
		participante.setTelefoneParticipante(dados.telefoneParticipante());
		participante.setStatusGeral(StatusGeral.ATIVO);
		
		participanteRepository.save(participante);
		
		return new DadosDetalharParticipante(participante);
	}
	
	@Transactional
	public DadosDetalharParticipante ativarParticipante(Long id) {
		Participante participante = participanteRepository.getReferenceById(id);
		participante.setStatusGeral(StatusGeral.ATIVO);
		
		return new DadosDetalharParticipante(participante);
	}
	
	@Transactional
	public DadosDetalharParticipante inativarParticipante(Long id) {
		Participante participante = participanteRepository.getReferenceById(id);
		participante.setStatusGeral(StatusGeral.INATIVO);
		
		return new DadosDetalharParticipante(participante);
	}
}
