package Projeto.Gerenciador_Eventos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import Projeto.Gerenciador_Eventos.dto.DadosAtualizarEvento;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvento;

    @Column(name = "nome_evento", nullable = false, length = 100)
    private String nomeEvento;

    @Column(name = "descricao_evento", columnDefinition = "TEXT")
    private String descricaoEvento;

    @Column(name = "data_evento", nullable = false)
    private LocalDate dataEvento;

    @Column(name = "local_evento", length = 200)
    private String localEvento;

    @Column(name = "vagas_totais_evento", nullable = false)
    private Integer vagasTotaisEvento;

    @Column(name = "vagas_disponiveis_evento", nullable = false)
    private Integer vagasDisponiveisEvento;

    @Column(name = "preco_evento", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoEvento;

    @Enumerated(EnumType.STRING) 
    @Column(name = "status_evento", nullable = false)
    private StatusGeral statusGeral;

    @ManyToOne
    @JoinColumn(name = "id_organizador", nullable = false)
    private Usuario organizador;
	
	public Evento() {
		
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
	public LocalDate getDataEvento() {
		return dataEvento;
	}
	public void setDataEvento(LocalDate dataEvento) {
		this.dataEvento = dataEvento;
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
	public StatusGeral getStatusGeral() {
		return statusGeral;
	}
	public void setStatusGeral(StatusGeral statusGeral) {
		this.statusGeral= statusGeral;
	}
	public Usuario getOrganizador() {
		return organizador;
	}
	public void setOrganizador(Usuario organizador) {
		this.organizador = organizador;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idEvento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		return Objects.equals(idEvento, other.idEvento);
	}

	public void atualizarInformações(@Valid DadosAtualizarEvento dados) {
		if (dados.dataEvento() != null) {
			this.dataEvento = dados.dataEvento();
		}
		if (dados.descricaoEvento() != null) {
			this.descricaoEvento = dados.descricaoEvento();
		}
		if (dados.localEvento() != null) {
			this.localEvento = dados.localEvento();
		}
		if (dados.nomeEvento() != null) {
			this.nomeEvento = dados.nomeEvento();
		}
		if (dados.precoEvento() != null) {
			this.precoEvento = dados.precoEvento();
		}
		if (dados.vagasDisponiveisEvento() != null) {
			this.vagasDisponiveisEvento = dados.vagasDisponiveisEvento();
		}
		if (dados.vagasTotaisEvento() != null) {
			this.vagasTotaisEvento = dados.vagasTotaisEvento();
		}
	}
}
