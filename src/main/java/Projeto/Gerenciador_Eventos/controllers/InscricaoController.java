package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroInscricao;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/inscricao")
public class InscricaoController {

	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosCadastroInscricao> cadastrarInscricao (@RequestBody @Valid DadosCadastroInscricao dados) {
		///
		/// 
		return ResponseEntity.ok().build();
	}
}
