package Projeto.Gerenciador_Eventos.dto;

import Projeto.Gerenciador_Eventos.entity.Usuario;
import Projeto.Gerenciador_Eventos.entity.enums.Perfil;

public record DadosDetalharUsuario(

		Long idUsuario,
		String nome,
		String login,
		String telefone,
		String cpf,
		Perfil perfil) {

	public DadosDetalharUsuario(Usuario usuario) {
		this(
				usuario.getIdUsuario(),
				usuario.getNome(),
				usuario.getLogin(),
				usuario.getTelefone(),
				usuario.getCpf(),
				usuario.getPerfil()
		);
	}
}
