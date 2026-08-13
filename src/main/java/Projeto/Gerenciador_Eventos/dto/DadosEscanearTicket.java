package Projeto.Gerenciador_Eventos.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosEscanearTicket(

		@NotBlank
		String codigoHashTicket) {

}
