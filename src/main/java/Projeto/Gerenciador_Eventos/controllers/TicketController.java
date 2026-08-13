package Projeto.Gerenciador_Eventos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroTicket;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharTicket;
import Projeto.Gerenciador_Eventos.dto.DadosListarTicket;
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
		
		var uri = uriBuilder.path("/ticket/detalhar/{id}").buildAndExpand(detalharDTO.idTicket()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
	
	@DeleteMapping("/inativar/{id}")
	@Transactional
	public ResponseEntity<DadosDetalharTicket> inativarTicket(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
		DadosDetalharTicket detalharDTO = ticketService.inativarTicket(id);
		
		var uri = uriBuilder.path("/ticket/detalhar/{id}").buildAndExpand(detalharDTO.idTicket()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
	
	@PutMapping("/reativar/{id}")
	@Transactional
	public ResponseEntity<DadosDetalharTicket> ativarTicket(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
		DadosDetalharTicket detalharDTO = ticketService.ativarTicket(id);
		
		var uri = uriBuilder.path("/ticket/detalhar/{id}").buildAndExpand(detalharDTO.idTicket()).toUri();
		
		return ResponseEntity.created(uri).body(detalharDTO);
	}
	
	@GetMapping("/detalhar/{id}")
	public ResponseEntity<DadosDetalharTicket> detalharTicket(@PathVariable Long id) {
		DadosDetalharTicket detalharDTO = ticketService.detalharTicket(id);
		
		return ResponseEntity.ok(detalharDTO);
	}
	
	@GetMapping(value = "/detalhar/{id}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> gerarQrCode(@PathVariable Long id) {
		byte[] imagemPng = ticketService.gerarQrCode(id);
		
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imagemPng);
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<DadosDetalharTicket>> listarTickets() {
	    List<DadosDetalharTicket> detalharDTO = ticketService.listarTickets();
	    
	    return ResponseEntity.ok(detalharDTO);
	}
	
	@GetMapping("/listar/filtro")
	public ResponseEntity<List<DadosDetalharTicket>> listarTicketsComParametros(@Valid DadosListarTicket parametros) {
	    List<DadosDetalharTicket> detalharDTO = ticketService.listarTicketsComParametros(parametros);
	    
	    return ResponseEntity.ok(detalharDTO);
	}
}