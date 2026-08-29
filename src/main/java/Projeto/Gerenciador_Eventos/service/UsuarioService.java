package Projeto.Gerenciador_Eventos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosAlterarSenha;
import Projeto.Gerenciador_Eventos.dto.DadosAtualizarUsuario;
import Projeto.Gerenciador_Eventos.dto.DadosCadastroUsuario;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharUsuario;
import Projeto.Gerenciador_Eventos.entity.Usuario;
import Projeto.Gerenciador_Eventos.entity.enums.Perfil;
import Projeto.Gerenciador_Eventos.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public void cadastrarUsuario(DadosCadastroUsuario dados) {
		if (usuarioRepository.findByLogin(dados.login()) != null) {
			throw new IllegalStateException("Já existe um usuário cadastrado com este login.");
		}

		if (usuarioRepository.existsByCpf(dados.cpf())) {
			throw new IllegalStateException("Já existe um usuário cadastrado com este CPF.");
		}

		Usuario usuario = new Usuario();
		usuario.setNome(dados.nome());
		usuario.setLogin(dados.login());
		usuario.setSenha(passwordEncoder.encode(dados.senha()));
		usuario.setCpf(dados.cpf());
		usuario.setTelefone(dados.telefone());

		// O primeiro usuário cadastrado no sistema vira ADMIN automaticamente
		// (bootstrap: sem isso, ninguém teria como virar admin no início).
		if (usuarioRepository.count() == 0) {
			usuario.setPerfil(Perfil.ADMIN);
		} else {
			usuario.setPerfil(Perfil.USER);
		}

		usuarioRepository.save(usuario);
	}

	public DadosDetalharUsuario detalharUsuarioLogado() {
		return new DadosDetalharUsuario(usuarioLogado());
	}

	@Transactional
	public DadosDetalharUsuario atualizarUsuario(DadosAtualizarUsuario dados) {
		Usuario usuario = usuarioLogado();

		if (dados.login() != null && !dados.login().equals(usuario.getLogin())) {
			if (usuarioRepository.findByLogin(dados.login()) != null) {
				throw new IllegalStateException("Já existe um usuário cadastrado com este login.");
			}
			usuario.setLogin(dados.login());
		}

		if (dados.nome() != null) {
			usuario.setNome(dados.nome());
		}

		if (dados.telefone() != null) {
			usuario.setTelefone(dados.telefone());
		}

		return new DadosDetalharUsuario(usuario);
	}

	@Transactional
	public void alterarSenha(DadosAlterarSenha dados) {
		Usuario usuario = usuarioLogado();

		if (!passwordEncoder.matches(dados.senhaAtual(), usuario.getPassword())) {
			throw new IllegalStateException("A senha atual informada está incorreta.");
		}

		usuario.setSenha(passwordEncoder.encode(dados.novaSenha()));
	}

	private Usuario usuarioLogado() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
