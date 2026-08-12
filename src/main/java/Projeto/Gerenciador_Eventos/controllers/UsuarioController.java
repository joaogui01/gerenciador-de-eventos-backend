package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroUsuario;
import Projeto.Gerenciador_Eventos.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping("/cadastrar")
	public ResponseEntity<Void> cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario dados) {
		usuarioService.cadastrarUsuario(dados);

		return ResponseEntity.ok().build();
	}

}