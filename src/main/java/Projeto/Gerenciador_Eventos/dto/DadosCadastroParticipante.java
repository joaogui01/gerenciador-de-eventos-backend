package Projeto.Gerenciador_Eventos.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroParticipante(	
		@NotBlank
		String nomeParticipante,
		
		@NotBlank
		String emailParticipante,
		
		@NotBlank
		String cpfParticipante,
		
		@NotBlank
		String telefoneParticipante) {

}
