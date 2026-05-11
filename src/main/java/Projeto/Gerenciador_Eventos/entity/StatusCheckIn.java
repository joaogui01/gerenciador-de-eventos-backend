package Projeto.Gerenciador_Eventos.entity;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroStatusCheckIn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table
@EqualsAndHashCode(of = "idStatusCheckIn")
public class StatusCheckIn {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idStatusCheckIn;
	
	private String descricaoStatusCheckIn;
	
	public StatusCheckIn() {
		
	}

	public StatusCheckIn(DadosCadastroStatusCheckIn dados) {
		this.descricaoStatusCheckIn = dados.descricaoStatusCheckIn();
	}
	
	public Integer getIdStatusCheckIn() {
		return idStatusCheckIn;
	}
	public void setIdStatusCheckIn(Integer idStatusCheckIn) {
		this.idStatusCheckIn = idStatusCheckIn;
	}
	public String getDescricaoStatusCheckIn() {
		return descricaoStatusCheckIn;
	}
	public void setDescricaoStatusCheckIn(String descricaoStatusCheckIn) {
		this.descricaoStatusCheckIn = descricaoStatusCheckIn;
	}
}

