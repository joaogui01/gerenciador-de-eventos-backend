package Projeto.Gerenciador_Eventos.entity;

import java.time.LocalDate;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroTicket;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table
@EqualsAndHashCode(of = "id")
public class Ticket {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idTicket;
	
	private Integer idInscricao;
	private String codigoHashTicket;
	private Integer idCheckIn;
	private LocalDate dataCheckInTicket;
	private Integer idStatus;
	
	public Ticket() {
		
	}
	
	public Ticket(DadosCadastroTicket dados) {
		this.idInscricao = dados.idInscricao();
		this.codigoHashTicket = dados.codigoHashTicket();
		this.idCheckIn = 2;
		this.dataCheckInTicket = dados.dataCheckInTicket();
		this.idStatus = 1;
	}

	public Integer getIdTicket() {
		return idTicket;
	}
	public void setIdTicket(Integer idTicket) {
		this.idTicket = idTicket;
	}
	public Integer getIdInscricao() {
		return idInscricao;
	}
	public void setIdInscricao(Integer idInscricao) {
		this.idInscricao = idInscricao;
	}
	public String getCodigoHashTicket() {
		return codigoHashTicket;
	}
	public void setCodigoHashTicket(String codigoHashTicket) {
		this.codigoHashTicket = codigoHashTicket;
	}
	public Integer getIdCheckIn() {
		return idCheckIn;
	}
	public void setIdCheckIn(Integer idCheckIn) {
		this.idCheckIn = idCheckIn;
	}
	public LocalDate getDataCheckInTicket() {
		return dataCheckInTicket;
	}
	public void setDataCheckInTicket(LocalDate dataCheckInTicket) {
		this.dataCheckInTicket = dataCheckInTicket;
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
