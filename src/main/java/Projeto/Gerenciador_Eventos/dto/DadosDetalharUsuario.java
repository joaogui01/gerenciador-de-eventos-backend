package Projeto.Gerenciador_Eventos.dto;

import Projeto.Gerenciador_Eventos.entity.Usuario;

public record DadosDetalharUsuario(

		Long idUsuario,
		String nome,
		String login,
		String telefone,
		String cpf) {

	public DadosDetalharUsuario(Usuario usuario) {
		this(
				usuario.getIdUsuario(),
				usuario.getNome(),
				usuario.getLogin(),
				usuario.getTelefone(),
				usuario.getCpf()
		);
	}
}
