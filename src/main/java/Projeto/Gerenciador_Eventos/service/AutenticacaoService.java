package Projeto.Gerenciador_Eventos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Projeto.Gerenciador_Eventos.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		UserDetails usuario = usuarioRepository.findByLogin(login);

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado: " + login);
		}

		return usuario;
	}

}