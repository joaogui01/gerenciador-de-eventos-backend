package Projeto.Gerenciador_Eventos.dto;

import java.time.LocalDateTime;

import Projeto.Gerenciador_Eventos.entity.Notificacao;

public record DadosDetalharNotificacao(

		Long idNotificacao,
		String titulo,
		String mensagem,
		boolean lida,
		LocalDateTime dataHora) {

	public DadosDetalharNotificacao(Notificacao notificacao) {
		this(
				notificacao.getIdNotificacao(),
				notificacao.getTitulo(),
				notificacao.getMensagem(),
				notificacao.isLida(),
				notificacao.getDataHora()
		);
	}
}
