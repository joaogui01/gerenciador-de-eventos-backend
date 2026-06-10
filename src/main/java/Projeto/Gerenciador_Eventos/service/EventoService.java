package Projeto.Gerenciador_Eventos.service;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroEvento;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharEvento;
import Projeto.Gerenciador_Eventos.entity.Evento;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;
import Projeto.Gerenciador_Eventos.repository.EventoRepository;
import jakarta.transaction.Transactional;

@Service
public class EventoService {
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Transactional 
	public DadosDetalharEvento cadastrarEvento(DadosCadastroEvento dados) {
		Evento evento = new Evento();
		evento.setNomeEvento(dados.nomeEvento());
		evento.setDescricaoEvento(dados.descricaoEvento());
		evento.setDataEvento(dados.dataEvento());
		evento.setLocalEvento(dados.localEvento());
		evento.setVagasTotaisEvento(dados.vagasTotaisEvento());
		evento.setVagasDisponiveisEvento(dados.vagasTotaisEvento());
		evento.setPrecoEvento(dados.precoEvento());
		evento.setStatusGeral(StatusGeral.ATIVO);
		
		eventoRepository.save(evento);
		
		return new DadosDetalharEvento(evento);
	}
	
	@Transactional
	public DadosDetalharEvento ativarEvento(Long id) {
		Evento evento = eventoRepository.getReferenceById(id);
		evento.setStatusGeral(StatusGeral.ATIVO);
		
		return new DadosDetalharEvento(evento);
	}
	
	@Transactional
	public DadosDetalharEvento inativarEvento(Long id) {
		Evento evento = eventoRepository.getReferenceById(id);
		evento.setStatusGeral(StatusGeral.INATIVO);
		
		return new DadosDetalharEvento(evento);
	}
}
