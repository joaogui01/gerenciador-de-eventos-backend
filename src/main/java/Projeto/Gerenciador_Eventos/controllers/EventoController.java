package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import Projeto.Gerenciador_Eventos.dto.DadosCadastroEvento;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/evento")
public class EventoController {

	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosCadastroEvento> cadastrarEvento (@RequestBody @Valid DadosCadastroEvento dados) {
		///
		/// 
		return ResponseEntity.ok().build();
	}
}
