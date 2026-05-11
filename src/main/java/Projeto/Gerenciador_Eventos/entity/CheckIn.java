package Projeto.Gerenciador_Eventos.entity;

import java.time.LocalDate;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroCheckIn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table
@EqualsAndHashCode(of = "idCheckIn")
public class CheckIn {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCheckIn;
	
	private LocalDate dataCheckInTicket;
	private Integer idTicket;
	private Integer idStatusCheckIn;
	
	public CheckIn() {
		
	}
	
	public CheckIn(DadosCadastroCheckIn dados) {
		this.dataCheckInTicket = dados.dataCheckInTicket();
		this.idTicket = dados.idTicket();
		this.idStatusCheckIn = dados.idStatusCheckIn();
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
	public Integer getIdTicket() {
		return idTicket;
	}
	public void setIdTicket(Integer idTicket) {
		this.idTicket = idTicket;
	}
	public Integer getIdStatusCheckIn() {
		return idStatusCheckIn;
	}
	public void setIdStatusCheckIn(Integer idStatusCheckIn) {
		this.idStatusCheckIn = idStatusCheckIn;
	}
}
