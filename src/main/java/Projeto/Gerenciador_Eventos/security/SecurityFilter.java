package Projeto.Gerenciador_Eventos.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import Projeto.Gerenciador_Eventos.repository.UsuarioRepository;
import Projeto.Gerenciador_Eventos.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String tokenJWT = recuperarToken(request);

		/*
		 * Token inválido, expirado ou de um usuário que não existe mais não pode derrubar
		 * a requisição com erro 500: a gente simplesmente não autentica ninguém e deixa o
		 * Spring Security responder 403. É isso que permite o frontend distinguir "sessão
		 * expirada" (aí descarta o token salvo e manda pro login) de "servidor com problema".
		 */
		if (tokenJWT != null) {
			try {
				String login = tokenService.getSubject(tokenJWT);
				UserDetails usuario = usuarioRepository.findByLogin(login);

				if (usuario != null) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (RuntimeException excecao) {
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(request, response);
	}

	private String recuperarToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return null;
		}

		return authorizationHeader.substring("Bearer ".length()).trim();
	}

}