package Projeto.Gerenciador_Eventos.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import Projeto.Gerenciador_Eventos.dto.DadosErroValidacao;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratadorDeErros {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> tratarErro404() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> tratarErro400(MethodArgumentNotValidException ex) {
		List<FieldError> erros = ex.getFieldErrors();
		List<DadosErroValidacao> listaErros = new ArrayList<>();

		for (FieldError erro : erros) {
			listaErros.add(new DadosErroValidacao(erro.getField(), erro.getDefaultMessage()));
		}

		return ResponseEntity.badRequest().body(listaErros);
	}

}