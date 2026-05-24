package Projeto.Gerenciador_Eventos.dto;

import Projeto.Gerenciador_Eventos.entity.Participante;

public record DadosDetalharParticipante(
		
		Long idParticipante,
	    String nomeParticipante,
	    String emailParticipante,
	    String cpfParticipante,
	    String telefoneParticipante) {
	
	public DadosDetalharParticipante(Participante participante) {
		this(
				participante.getIdParticipante(),
				participante.getNomeParticipante(),
				participante.getEmailParticipante(),
				participante.getCpfParticipante(),
				participante.getTelefoneParticipante()
		);
	}
}
