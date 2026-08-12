package Projeto.Gerenciador_Eventos.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import Projeto.Gerenciador_Eventos.entity.Usuario;

@Service
public class TokenService {

	@Value("${api.security.token.secret}")
	private String secret;

	public String gerarToken(Usuario usuario) {
		try {
			Algorithm algoritmo = Algorithm.HMAC256(secret);

			String token = JWT.create()
					.withIssuer("gerenciador-eventos-api")
					.withSubject(usuario.getLogin())
					.withExpiresAt(dataExpiracao())
					.sign(algoritmo);

			return token;
		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar o token JWT.", exception);
		}
	}

	public String getSubject(String tokenJWT) {
		try {
			Algorithm algoritmo = Algorithm.HMAC256(secret);

			String subject = JWT.require(algoritmo)
					.withIssuer("gerenciador-eventos-api")
					.build()
					.verify(tokenJWT)
					.getSubject();

			return subject;
		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Token JWT inválido ou expirado.", exception);
		}
	}

	private Instant dataExpiracao() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}

}