package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroStatusCheckIn;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/statuscheckin")
public class StatusCheckInController {

	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosCadastroStatusCheckIn> cadastrarStatusCheckIn (@RequestBody @Valid DadosCadastroStatusCheckIn dados) {
		///
		/// 
		return ResponseEntity.ok().build();
	}
}
