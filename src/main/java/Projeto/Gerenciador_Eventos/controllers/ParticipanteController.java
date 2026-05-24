package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroParticipante;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharParticipante;
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
}
