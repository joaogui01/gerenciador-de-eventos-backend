package Projeto.Gerenciador_Eventos.dto;

import Projeto.Gerenciador_Eventos.entity.Participante;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;

public record DadosListarParticipante( 
			
	    String nomeParticipante,
	    String emailParticipante,
	    String cpfParticipante,
	    String telefoneParticipante,
	    StatusGeral statusGeral) {
	
	public DadosListarParticipante(Participante participante) {
		this(
				participante.getNomeParticipante(),
				participante.getEmailParticipante(),
				participante.getCpfParticipante(),
				participante.getTelefoneParticipante(),
				participante.getStatusGeral()
		);
	}
}
