package Projeto.Gerenciador_Eventos.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosDetalharNotificacao;
import Projeto.Gerenciador_Eventos.entity.Notificacao;
import Projeto.Gerenciador_Eventos.entity.Usuario;
import Projeto.Gerenciador_Eventos.repository.NotificacaoRepository;
import jakarta.transaction.Transactional;

@Service
public class NotificacaoService {

	@Autowired
	private NotificacaoRepository notificacaoRepository;

	// Chamado internamente por outros services quando acontece algo que o destinatário
	// precisa saber (hoje, só quando alguém se inscreve num evento — ver InscricaoService).
	// Não é exposto como endpoint público: ninguém deve conseguir mandar notificação
	// pra qualquer pessoa via API.
	@Transactional
	public void criarNotificacao(Usuario destinatario, String titulo, String mensagem) {
		Notificacao notificacao = new Notificacao();
		notificacao.setUsuario(destinatario);
		notificacao.setTitulo(titulo);
		notificacao.setMensagem(mensagem);
		notificacao.setLida(false);
		notificacao.setDataHora(LocalDateTime.now());

		notificacaoRepository.save(notificacao);
	}

	public List<DadosDetalharNotificacao> listarMinhasNotificacoes() {
		List<Notificacao> notificacoes = notificacaoRepository.findByUsuarioOrderByDataHoraDesc(usuarioLogado());
		List<DadosDetalharNotificacao> detalharDTOs = new ArrayList<>();

		for (Notificacao notificacao : notificacoes) {
			detalharDTOs.add(new DadosDetalharNotificacao(notificacao));
		}

		return detalharDTOs;
	}

	// Pensado pro selo de "notificação nova" no sino do app — o frontend chama isso
	// periodicamente (polling) pra saber se precisa mostrar o indicador.
	public long contarNaoLidas() {
		return notificacaoRepository.countByUsuarioAndLidaFalse(usuarioLogado());
	}

	@Transactional
	public void marcarComoLida(Long id) {
		Notificacao notificacao = notificacaoRepository.getReferenceById(id);

		Usuario usuarioLogado = usuarioLogado();
		if (!notificacao.getUsuario().getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
			throw new AccessDeniedException("Você não tem permissão para marcar esta notificação.");
		}

		notificacao.setLida(true);
	}

	private Usuario usuarioLogado() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
