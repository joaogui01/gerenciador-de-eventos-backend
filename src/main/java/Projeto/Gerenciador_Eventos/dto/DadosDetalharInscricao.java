package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDate;
import Projeto.Gerenciador_Eventos.entity.Inscricao;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;


public record DadosDetalharInscricao(
		
		Long idInscricao,
		Long idEvento,
		String nomeEvento,
		String localEvento,
		LocalDate dataEvento,
		Long idParticipante,
		String nomeParticipante,
		LocalDate dataInscricao,
		StatusGeral statusGeral) {

	public DadosDetalharInscricao(Inscricao inscricao) {
		this(
				inscricao.getIdInscricao(), 
				inscricao.getEvento().getIdEvento(), 
				inscricao.getEvento().getNomeEvento(),
				inscricao.getEvento().getLocalEvento(),
				inscricao.getEvento().getDataEvento(),
				inscricao.getParticipante().getIdUsuario(), 
				inscricao.getParticipante().getNome(),
				inscricao.getDataInscricao(),
				inscricao.getStatusGeral()
		);
	}
}
