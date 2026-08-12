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

import Projeto.Gerenciador_Eventos.dto.DadosCadastroParticipante;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharParticipante;
import Projeto.Gerenciador_Eventos.dto.DadosListarParticipante;
import Projeto.Gerenciador_Eventos.service.ParticipanteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/participante")
public class ParticipanteController {
	
	@Autowired
	private ParticipanteService participanteService;
	
	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosDetalharParticipante> cadastrarParticipante (@RequestBody @Valid DadosCadastroParticipante dados, UriComponentsBuilder uriBuilder) {
		DadosDetalharParticipante detalharDTO = participanteService.cadastrarParticipante(dados);
		
		var uri = uriBuilder.path("/participante/detalhar/{id}").buildAndExpand(detalharDTO.idParticipante()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
	
	@DeleteMapping("/inativar/{id}")
	@Transactional
	public ResponseEntity<DadosDetalharParticipante> inativarParticipante(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
		DadosDetalharParticipante detalharDTO = participanteService.inativarParticipante(id);
		
		var uri = uriBuilder.path("/participante/detalhar/{id}").buildAndExpand(detalharDTO.idParticipante()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
	
	@PutMapping("/reativar/{id}")
	@Transactional
	public ResponseEntity<DadosDetalharParticipante> ativarParticipante(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
		DadosDetalharParticipante detalharDTO = participanteService.ativarParticipante(id);
		
		var uri = uriBuilder.path("/participante/detalhar/{id}").buildAndExpand(detalharDTO.idParticipante()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
	
	@GetMapping("/detalhar/{id}")
	public ResponseEntity<DadosDetalharParticipante> detalharParticipante(@PathVariable Long id) {
		DadosDetalharParticipante detalharDTO = participanteService.detalharParticipante(id);
		
		return ResponseEntity.ok(detalharDTO);
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<DadosDetalharParticipante>> listarParticipantes() {
	    List<DadosDetalharParticipante> detalharDTO = participanteService.listarParticipantes();
	    
	    return ResponseEntity.ok(detalharDTO);
	}
	
	@GetMapping("/listar/filtro")
	public ResponseEntity<List<DadosDetalharParticipante>> listarParticipantesComParametros(@Valid DadosListarParticipante parametros) {
	    List<DadosDetalharParticipante> detalharDTO = participanteService.listarParticipantesComParametros(parametros);
	    
	    return ResponseEntity.ok(detalharDTO);
	}
}