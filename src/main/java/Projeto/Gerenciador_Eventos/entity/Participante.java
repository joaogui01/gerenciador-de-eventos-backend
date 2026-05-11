package Projeto.Gerenciador_Eventos.entity;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroParticipante;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table
@EqualsAndHashCode(of = "idParticipante")
public class Participante {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idParticipante;
	
	private String nomeParticipante;
	private String emailParticipante;
	private String cpfParticipante;
	private String telefoneParticipante;
	private Integer idStatus;
	
	public Participante() {
		
	}
	
	public Participante(DadosCadastroParticipante dados) {
		this.nomeParticipante = dados.nomeParticipante();
		this.emailParticipante = dados.emailParticipante();
		this.cpfParticipante = dados.cpfParticipante();
		this.telefoneParticipante = dados.telefoneParticipante();
		this.idStatus = 1;
	}

	public Integer getIdParticipante() {
		return idParticipante;
	}
	public void setIdParticipante(Integer idParticipante) {
		this.idParticipante = idParticipante;
	}
	public String getNomeParticipante() {
		return nomeParticipante;
	}
	public void setNomeParticipante(String nomeParticipante) {
		this.nomeParticipante = nomeParticipante;
	}
	public String getEmailParticipante() {
		return emailParticipante;
	}
	public void setEmailParticipante(String emailParticipante) {
		this.emailParticipante = emailParticipante;
	}
	public String getCpfParticipante() {
		return cpfParticipante;
	}
	public void setCpfParticipante(String cpfParticipante) {
		this.cpfParticipante = cpfParticipante;
	}
	public String getTelefoneParticipante() {
		return telefoneParticipante;
	}
	public void setTelefoneParticipante(String telefoneParticipante) {
		this.telefoneParticipante = telefoneParticipante;
	}
	public Integer getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}
	public void inativar() {
		this.idStatus = 2;
	}
	public void reativar() {
		this.idStatus = 1;
	}
}
