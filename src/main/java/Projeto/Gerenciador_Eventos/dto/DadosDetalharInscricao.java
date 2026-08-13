package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDate;
import Projeto.Gerenciador_Eventos.entity.Inscricao;


public record DadosDetalharInscricao(
		
		Long idInscricao,
		Long idEvento,
		Long idParticipante,
		String nomeParticipante,
		LocalDate dataInscricao) {

	public DadosDetalharInscricao(Inscricao inscricao) {
		this(
				inscricao.getIdInscricao(), 
				inscricao.getEvento().getIdEvento(), 
				inscricao.getParticipante().getIdUsuario(), 
				inscricao.getParticipante().getNome(),
				inscricao.getDataInscricao()
		);
	}
}
