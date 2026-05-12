package Projeto.Gerenciador_Eventos.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import Projeto.Gerenciador_Eventos.entity.enums.StatusCheckIn;
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
@Table(name = "checkin")
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCheckIn;

    @OneToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;

    @Column(name = "data_checkin",nullable = false)
    private LocalDateTime dataCheckIn;

    @Enumerated(EnumType.STRING) 
    @Column(name = "status_checkin", nullable = false)
    private StatusCheckIn statusCheckIn;
	
	public CheckIn() {
		
	}
	
	public Long getIdCheckIn() {
		return idCheckIn;
	}
	public void setIdCheckIn(Long idCheckIn) {
		this.idCheckIn = idCheckIn;
	}
	public Ticket getTicket() {
		return ticket;
	}
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	public LocalDateTime getDataCheckIn() {
		return dataCheckIn;
	}
	public void setDataCheckIn(LocalDateTime dataCheckIn) {
		this.dataCheckIn = dataCheckIn;
	}
	public StatusCheckIn getStatusCheckIn() {
		return statusCheckIn;
	}
	public void setStatusCheckIn(StatusCheckIn statusCheckIn) {
		this.statusCheckIn = statusCheckIn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idCheckIn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CheckIn other = (CheckIn) obj;
		return Objects.equals(idCheckIn, other.idCheckIn);
	}
}
