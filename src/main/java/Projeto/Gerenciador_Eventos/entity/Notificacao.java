package Projeto.Gerenciador_Eventos.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificacao")
public class Notificacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idNotificacao;

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	@Column(name = "titulo", nullable = false, length = 150)
	private String titulo;

	@Column(name = "mensagem", nullable = false, length = 500)
	private String mensagem;

	@Column(name = "lida", nullable = false)
	private boolean lida;

	@Column(name = "data_hora", nullable = false)
	private LocalDateTime dataHora;

	public Notificacao() {

	}

	public Long getIdNotificacao() {
		return idNotificacao;
	}
	public void setIdNotificacao(Long idNotificacao) {
		this.idNotificacao = idNotificacao;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public boolean isLida() {
		return lida;
	}
	public void setLida(boolean lida) {
		this.lida = lida;
	}
	public LocalDateTime getDataHora() {
		return dataHora;
	}
	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

}
