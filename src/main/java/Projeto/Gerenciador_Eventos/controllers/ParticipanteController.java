package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroParticipante;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/participante")
public class ParticipanteController {

	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosCadastroParticipante> cadastrarParticipante (@RequestBody @Valid DadosCadastroParticipante dados) {
		///
		/// 
		return ResponseEntity.ok().build();
	}
}
