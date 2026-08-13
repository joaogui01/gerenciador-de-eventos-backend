package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDate;

import Projeto.Gerenciador_Eventos.entity.Inscricao;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;

public record DadosListarInscricao(	
		
		Long idEvento,
		Long idParticipante,
		LocalDate dataInscricao,
		StatusGeral statusGeral) {

	public DadosListarInscricao(Inscricao inscricao) {
		this(
				inscricao.getEvento().getIdEvento(), 
				inscricao.getParticipante().getIdUsuario(), 
				inscricao.getDataInscricao(),
				inscricao.getStatusGeral()
		);
	}
}
