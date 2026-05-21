package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroInscricao;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharInscricao;
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
}
