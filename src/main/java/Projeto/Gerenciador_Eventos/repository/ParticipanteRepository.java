package Projeto.Gerenciador_Eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Projeto.Gerenciador_Eventos.entity.Participante;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
	@Query("SELECT p FROM Participante p WHERE " +
	           "(:nome IS NULL OR p.nomeParticipante LIKE %:nome%) AND " +
	           "(:email IS NULL OR p.emailParticipante = :email) AND " +
	           "(:cpf IS NULL OR p.cpfParticipante = :cpf) AND " +
	           "(:telefone IS NULL OR p.telefoneParticipante = :telefone) AND " +
	           "(:status IS NULL OR p.statusGeral = :status)")
	List<Participante> buscarComFiltrosDinamicos(
	        @Param("nome") String nome, 
	        @Param("email") String email, 
	        @Param("cpf") String cpf, 
	        @Param("telefone") String telefone, 
	        @Param("status") StatusGeral status
	);
}
