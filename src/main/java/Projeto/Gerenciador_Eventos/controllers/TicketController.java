package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroTicket;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ticket")
public class TicketController {

	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosCadastroTicket> cadastrarTicket (@RequestBody @Valid DadosCadastroTicket dados) {
		///
		/// 
		return ResponseEntity.ok().build();
	}
}
