package Projeto.Gerenciador_Eventos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import Projeto.Gerenciador_Eventos.dto.DadosCadastroEvento;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table
@EqualsAndHashCode(of = "id")
public class Evento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEvento;
	
	private String nomeEvento;
	private String descricaoEvento;
	private LocalDateTime dataHorarioEvento;
	private String localEvento;
	private Integer vagasTotaisEvento;
	private Integer vagasDisponiveisEvento;
	private BigDecimal precoEvento;
	private Integer idStatus;
	
	public Evento() {
		
	}
	
	public Evento(DadosCadastroEvento dados) {
		this.nomeEvento = dados.nomeEvento();
		this.descricaoEvento = dados.descricaoEvento();
		this.dataHorarioEvento = dados.dataHorarioEvento();
		this.localEvento = dados.localEvento();
		this.vagasTotaisEvento = dados.vagasTotaisEvento();
		this.vagasDisponiveisEvento = dados.vagasDisponiveisEvento();
		this.precoEvento = dados.precoEvento();
		this.idStatus = 1;
	}

	public Long getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(Long idEvento) {
		this.idEvento = idEvento;
	}
	public String getNomeEvento() {
		return nomeEvento;
	}
	public void setNomeEvento(String nomeEvento) {
		this.nomeEvento = nomeEvento;
	}
	public String getDescricaoEvento() {
		return descricaoEvento;
	}
	public void setDescricaoEvento(String descricaoEvento) {
		this.descricaoEvento = descricaoEvento;
	}
	public LocalDateTime getDataHorarioEvento() {
		return dataHorarioEvento;
	}
	public void setDataHorarioEvento(LocalDateTime dataHorarioEvento) {
		this.dataHorarioEvento = dataHorarioEvento;
	}
	public String getLocalEvento() {
		return localEvento;
	}
	public void setLocalEvento(String localEvento) {
		this.localEvento = localEvento;
	}
	public Integer getVagasTotaisEvento() {
		return vagasTotaisEvento;
	}
	public void setVagasTotaisEvento(Integer vagasTotaisEvento) {
		this.vagasTotaisEvento = vagasTotaisEvento;
	}
	public Integer getVagasDisponiveisEvento() {
		return vagasDisponiveisEvento;
	}
	public void setVagasDisponiveisEvento(Integer vagasDisponiveisEvento) {
		this.vagasDisponiveisEvento = vagasDisponiveisEvento;
	}
	public BigDecimal getPrecoEvento() {
		return precoEvento;
	}
	public void setPrecoEvento(BigDecimal precoEvento) {
		this.precoEvento = precoEvento;
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
