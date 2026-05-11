package Projeto.Gerenciador_Eventos.entity;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table
@EqualsAndHashCode(of = "idStatus")
public class Status {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idStatus;
	
	private String descricaoStatus;
	
	public Status() {
		
	}

	public Status(DadosCadastroStatus dados) {
		this.descricaoStatus = dados.descricaoStatus();
	}
	
	public Integer getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}
	public String getDescricaoStatus() {
		return descricaoStatus;
	}
	public void setDescricaoStatus(String descricaoStatus) {
		this.descricaoStatus = descricaoStatus;
	}
}
