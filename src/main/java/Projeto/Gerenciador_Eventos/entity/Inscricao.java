package Projeto.Gerenciador_Eventos.entity;

import java.time.LocalDate;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroInscricao;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table
@EqualsAndHashCode(of = "idInscricao")
public class Inscricao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idInscricao;
	
	private Integer idEvento;
	private Integer idParticipante;
	private LocalDate dataInscricao;
	private Integer idStatus;
	
	public Inscricao() {
		
	}
	
	public Inscricao(DadosCadastroInscricao dados) {
		this.idEvento = dados.idEvento();
		this.idParticipante = dados.idParticipante();
		this.dataInscricao = dados.dataInscricao();
		this.idStatus = 1;
	}

	public Integer getIdInscricao() {
		return idInscricao;
	}
	public void setIdInscricao(Integer idInscricao) {
		this.idInscricao = idInscricao;
	}
	public Integer getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(Integer idEvento) {
		this.idEvento = idEvento;
	}
	public Integer getIdParticipante() {
		return idParticipante;
	}
	public void setIdParticipante(Integer idParticipante) {
		this.idParticipante = idParticipante;
	}
	public LocalDate getDataInscricao() {
		return dataInscricao;
	}
	public Integer getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}
	public void setDataInscricao(LocalDate dataInscricao) {
		this.dataInscricao = dataInscricao;
	}
	public void inativar() {
		this.idStatus = 2;
	}
	public void reativar() {
		this.idStatus = 1;
	}
}
