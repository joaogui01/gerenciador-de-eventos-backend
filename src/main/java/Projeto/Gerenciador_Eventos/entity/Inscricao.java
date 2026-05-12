package Projeto.Gerenciador_Eventos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;

@Entity
@Table(name = "inscricao")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInscricao;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "id_participante", nullable = false)
    private Participante participante;

    @Column(name = "data_inscricao", nullable = false)
    private LocalDateTime dataInscricao;

    @Enumerated(EnumType.STRING) 
    @Column(name = "status_inscricao", nullable = false)
    private StatusGeral statusGeral;

    public Inscricao() {
    }

    public Long getIdInscricao() {
        return idInscricao;
    }
    public void setIdInscricao(Long idInscricao) {
        this.idInscricao = idInscricao;
    }
    public Evento getEvento() {
        return evento;
    }
    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    public Participante getParticipante() {
        return participante;
    }
    public void setParticipante(Participante participante) {
        this.participante = participante;
    }
    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }
    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }
	public StatusGeral getStatusGeral() {
		return statusGeral;
	}
	public void setStatusGeral(StatusGeral statusGeral) {
		this.statusGeral= statusGeral;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idInscricao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Inscricao other = (Inscricao) obj;
		return Objects.equals(idInscricao, other.idInscricao);
	}
}
