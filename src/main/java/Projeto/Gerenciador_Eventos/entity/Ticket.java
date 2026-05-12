package Projeto.Gerenciador_Eventos.entity;


import java.util.Objects;

import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    @OneToOne
    @JoinColumn(name = "id_inscricao", nullable = false)
    private Inscricao inscricao;

    @Column(nullable = false, unique = true)
    private String codigoHashTicket;

    @Enumerated(EnumType.STRING) 
    @Column(name = "status_ticket", nullable = false)
    private StatusGeral statusGeral;
	
	public Ticket() {
		
	}
	
	public Long getIdTicket() {
		return idTicket;
	}
	public void setIdTicket(Long idTicket) {
		this.idTicket = idTicket;
	}
	public Inscricao getInscricao() {
		return inscricao;
	}
	public void setInscricao(Inscricao inscricao) {
		this.inscricao = inscricao;
	}
	public String getCodigoHashTicket() {
		return codigoHashTicket;
	}
	public void setCodigoHashTicket(String codigoHashTicket) {
		this.codigoHashTicket = codigoHashTicket;
	}
	public StatusGeral getStatusGeral() {
		return statusGeral;
	}
	public void setStatusGeral(StatusGeral statusGeral) {
		this.statusGeral= statusGeral;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idTicket);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		return Objects.equals(idTicket, other.idTicket);
	}
}
