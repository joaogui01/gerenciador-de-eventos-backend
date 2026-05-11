package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroCheckIn;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/checkin")
public class CheckInController {

	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosCadastroCheckIn> cadastrarCheckIn (@RequestBody @Valid DadosCadastroCheckIn dados) {
		///
		/// 
		return ResponseEntity.ok().build();
	}

}
