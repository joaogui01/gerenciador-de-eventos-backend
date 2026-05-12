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
import jakarta.persistence.Table;

@Entity
@Table(name = "participante")
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idParticipante;

    @Column(name = "nome_participante", nullable = false, length = 150)
    private String nomeParticipante;

    @Column(name = "email_participante", nullable = false, unique = true, length = 100)
    private String emailParticipante;

    @Column(name = "cpf_participante", nullable = false, unique = true, length = 11)
    private String cpfParticipante;

    @Column(name = "telefone_participante", length = 20)
    private String telefoneParticipante;

    @Enumerated(EnumType.STRING) 
    @Column(name = "status_participante", nullable = false)
    private StatusGeral statusGeral;

	public Participante() {
		
	}
	
	public Long getIdParticipante() {
		return idParticipante;
	}
	public void setIdParticipante(Long idParticipante) {
		this.idParticipante = idParticipante;
	}
	public String getNomeParticipante() {
		return nomeParticipante;
	}
	public void setNomeParticipante(String nomeParticipante) {
		this.nomeParticipante = nomeParticipante;
	}
	public String getEmailParticipante() {
		return emailParticipante;
	}
	public void setEmailParticipante(String emailParticipante) {
		this.emailParticipante = emailParticipante;
	}
	public String getCpfParticipante() {
		return cpfParticipante;
	}
	public void setCpfParticipante(String cpfParticipante) {
		this.cpfParticipante = cpfParticipante;
	}
	public String getTelefoneParticipante() {
		return telefoneParticipante;
	}
	public void setTelefoneParticipante(String telefoneParticipante) {
		this.telefoneParticipante = telefoneParticipante;
	}
	public StatusGeral getStatusGeral() {
		return statusGeral;
	}
	public void setStatusGeral(StatusGeral statusGeral) {
		this.statusGeral= statusGeral;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idParticipante);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Participante other = (Participante) obj;
		return Objects.equals(idParticipante, other.idParticipante);
	}
}
