package Projeto.Gerenciador_Eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Projeto.Gerenciador_Eventos.dto.DadosAlterarSenha;
import Projeto.Gerenciador_Eventos.dto.DadosAtualizarUsuario;
import Projeto.Gerenciador_Eventos.dto.DadosCadastroUsuario;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharUsuario;
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

	@GetMapping("/meuperfil")
	public ResponseEntity<DadosDetalharUsuario> detalharUsuarioLogado() {
		return ResponseEntity.ok(usuarioService.detalharUsuarioLogado());
	}

	@PutMapping("/atualizar")
	public ResponseEntity<DadosDetalharUsuario> atualizarUsuario(@RequestBody DadosAtualizarUsuario dados) {
		return ResponseEntity.ok(usuarioService.atualizarUsuario(dados));
	}

	@PutMapping("/alterar-senha")
	public ResponseEntity<Void> alterarSenha(@RequestBody @Valid DadosAlterarSenha dados) {
		usuarioService.alterarSenha(dados);

		return ResponseEntity.ok().build();
	}

}