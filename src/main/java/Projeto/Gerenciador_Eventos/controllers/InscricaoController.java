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

import Projeto.Gerenciador_Eventos.dto.DadosCadastroInscricao;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharInscricao;
import Projeto.Gerenciador_Eventos.dto.DadosListarInscricao;
import Projeto.Gerenciador_Eventos.service.InscricaoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/inscricao")
public class InscricaoController {

	@Autowired
	private InscricaoService inscricaoService;
	
	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosDetalharInscricao> cadastrarInscricao(@RequestBody @Valid DadosCadastroInscricao dados, UriComponentsBuilder uriBuilder) {
		DadosDetalharInscricao detalharDTO = inscricaoService.cadastrarInscricao(dados);
		
		var uri = uriBuilder.path("/inscricao/detalhar/{id}").buildAndExpand(detalharDTO.idInscricao()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
	
	@DeleteMapping("/inativar/{id}")
	@Transactional
	public ResponseEntity<DadosDetalharInscricao> inativarInscricao(@PathVariable Long id) {
		DadosDetalharInscricao detalharDTO = inscricaoService.inativarInscricao(id);
		
		return ResponseEntity.ok(detalharDTO);
	}
	
	@PutMapping("/reativar/{id}")
	@Transactional
	public ResponseEntity<DadosDetalharInscricao> ativarInscricao(@PathVariable Long id) {
		DadosDetalharInscricao detalharDTO = inscricaoService.ativarInscricao(id);
		
		return ResponseEntity.ok(detalharDTO);
	}
	
	@GetMapping("/detalhar/{id}")
	public ResponseEntity<DadosDetalharInscricao> detalharInscricao(@PathVariable Long id) {
		DadosDetalharInscricao detalharDTO = inscricaoService.detalharInscricao(id);
		
		return ResponseEntity.ok(detalharDTO);
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<DadosDetalharInscricao>> listarInscricoes() {
	    List<DadosDetalharInscricao> detalharDTO = inscricaoService.listarInscricoes();
	    
	    return ResponseEntity.ok(detalharDTO);
	}
	
	@GetMapping("/listar/filtro")
	public ResponseEntity<List<DadosDetalharInscricao>> listarInscricoesComParametros(@Valid DadosListarInscricao parametros) {
	    List<DadosDetalharInscricao> detalharDTO = inscricaoService.listarInscricoesComParametros(parametros);
	    
	    return ResponseEntity.ok(detalharDTO);
	}
}