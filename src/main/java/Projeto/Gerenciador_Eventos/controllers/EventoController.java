package Projeto.Gerenciador_Eventos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroEvento;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharEvento;
import Projeto.Gerenciador_Eventos.service.EventoService;
import jakarta.transaction.Transactional;
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
	
	@DeleteMapping("/inativar/{id}")
	@Transactional
	public ResponseEntity<DadosDetalharEvento> inativarEvento(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
		DadosDetalharEvento detalharDTO = eventoService.inativarEvento(id);
		
		var uri = uriBuilder.path("/evento/detalhar/{id}").buildAndExpand(detalharDTO.idEvento()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
	
	@PutMapping("/reativar/{id}")
	@Transactional
	public ResponseEntity<DadosDetalharEvento> ativarEvento(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
		DadosDetalharEvento detalharDTO = eventoService.ativarEvento(id);
		
		var uri = uriBuilder.path("/evento/detalhar/{id}").buildAndExpand(detalharDTO.idEvento()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<DadosDetalharEvento>> listarEventos() {
	    List<DadosDetalharEvento> detalharDTO = eventoService.listarEventos();
	    
	    return ResponseEntity.ok(detalharDTO);
	}
}
