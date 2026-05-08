package Projeto.Gerenciador_Eventos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroParticipante(	
		@NotBlank
		String nomeParticipante,
		
		@NotBlank
		String emailParticipante,
		
		@NotBlank
		String cpfParticipante,
		
		@NotBlank
		String telefoneParticipante,
		
		@NotNull
		Integer idStatus) {

}
