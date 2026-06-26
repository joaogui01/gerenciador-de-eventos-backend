package Projeto.Gerenciador_Eventos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import Projeto.Gerenciador_Eventos.entity.Evento;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;

public record DadosListarEvento(	
		String nomeEvento,
		String descricaoEvento,
		LocalDate dataEvento,
		String localEvento,
		Integer vagasTotaisEvento,
		Integer vagasDisponiveisEvento,
		BigDecimal precoEvento,
		StatusGeral statusGeral) {
	
	public DadosListarEvento(Evento evento) {
		this(
			evento.getNomeEvento(),
			evento.getDescricaoEvento(),
			evento.getDataEvento(),
			evento.getLocalEvento(),
			evento.getVagasTotaisEvento(),
			evento.getVagasDisponiveisEvento(),
			evento.getPrecoEvento(),
			evento.getStatusGeral()
		);
	}
}