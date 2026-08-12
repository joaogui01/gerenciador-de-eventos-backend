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

		if (tokenJWT != null) {
			String login = tokenService.getSubject(tokenJWT);
			UserDetails usuario = usuarioRepository.findByLogin(login);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String recuperarToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader == null) {
			return null;
		}

		return authorizationHeader.replace("Bearer ", "");
	}

}