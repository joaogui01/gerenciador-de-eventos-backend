package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroTicket;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharTicket;
import Projeto.Gerenciador_Eventos.service.TicketService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ticket")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	@PostMapping("/cadastrar")
	@Transactional
	public ResponseEntity<DadosDetalharTicket> cadastrarTicket (@RequestBody @Valid DadosCadastroTicket dados, UriComponentsBuilder uriBuilder) {
		DadosDetalharTicket detalharDTO = ticketService.cadastrarTicket(dados);
		
		var uri = uriBuilder.path("/ticket/cadastrar/{id}").buildAndExpand(detalharDTO.idTicket()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
}
