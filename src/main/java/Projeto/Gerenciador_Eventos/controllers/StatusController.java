package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/status")
public class StatusController {

	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosCadastroStatus> cadastrarStatus (@RequestBody @Valid DadosCadastroStatus dados) {
		///
		/// 
		return ResponseEntity.ok().build();
	}
}
