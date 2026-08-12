package Projeto.Gerenciador_Eventos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroUsuario;
import Projeto.Gerenciador_Eventos.entity.Usuario;
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

		Usuario usuario = new Usuario();
		usuario.setLogin(dados.login());
		usuario.setSenha(passwordEncoder.encode(dados.senha()));

		usuarioRepository.save(usuario);
	}

}