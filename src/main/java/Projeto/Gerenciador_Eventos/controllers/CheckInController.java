package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroCheckIn;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharCheckIn;
import Projeto.Gerenciador_Eventos.service.CheckInService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/checkin")
public class CheckInController {

	@Autowired
	private CheckInService checkInService;
	
	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosDetalharCheckIn> cadastrarCheckIn (@RequestBody @Valid DadosCadastroCheckIn dados, UriComponentsBuilder uriBuilder) {
		DadosDetalharCheckIn detalharDTO = checkInService.cadastrarCheckIn(dados);
		
		var uri = uriBuilder.path("/checkin/cadastrar/{id}").buildAndExpand(detalharDTO.idCheckIn()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
	
	@PutMapping("/realizarcheckin/{id}")
	@Transactional
	public ResponseEntity<DadosDetalharCheckIn> realizarCheckIn (@PathVariable Long id, UriComponentsBuilder uriBuilder) {
		DadosDetalharCheckIn detalharDTO = checkInService.realizarCheckIn(id);
		
		var uri = uriBuilder.path("/checkin/detalhar/{id}").buildAndExpand(detalharDTO.idCheckIn()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
}
