package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroEvento;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharEvento;
import Projeto.Gerenciador_Eventos.service.EventoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/evento")
public class EventoController {

	@Autowired
	private EventoService eventoService;
	
	@PostMapping("/cadastrar")
	public ResponseEntity<DadosDetalharEvento> cadastrarEvento(@RequestBody @Valid DadosCadastroEvento dados, UriComponentsBuilder uriBuilder) {
		DadosDetalharEvento detalharDTO = eventoService.cadastrarEvento(dados);
		
		var uri = uriBuilder.path("/evento/detalhar/{id}").buildAndExpand(detalharDTO.idEvento()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
}
