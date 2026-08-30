package Projeto.Gerenciador_Eventos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Projeto.Gerenciador_Eventos.dto.DadosDetalharNotificacao;
import Projeto.Gerenciador_Eventos.service.NotificacaoService;

@RestController
@RequestMapping("/notificacao")
public class NotificacaoController {

	@Autowired
	private NotificacaoService notificacaoService;

	@GetMapping("/listar")
	public ResponseEntity<List<DadosDetalharNotificacao>> listarMinhasNotificacoes() {
		return ResponseEntity.ok(notificacaoService.listarMinhasNotificacoes());
	}

	@GetMapping("/naolidas/contador")
	public ResponseEntity<Long> contarNaoLidas() {
		return ResponseEntity.ok(notificacaoService.contarNaoLidas());
	}

	@PutMapping("/marcarlida/{id}")
	public ResponseEntity<Void> marcarComoLida(@PathVariable Long id) {
		notificacaoService.marcarComoLida(id);

		return ResponseEntity.ok().build();
	}

}
