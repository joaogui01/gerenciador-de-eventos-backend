package Projeto.Gerenciador_Eventos.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Projeto.Gerenciador_Eventos.entity.Evento;
import Projeto.Gerenciador_Eventos.entity.Inscricao;
import Projeto.Gerenciador_Eventos.entity.Participante;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
	@Query("SELECT i FROM Inscricao i WHERE " +
	           "(:evento IS NULL OR i.evento = :evento) AND " +
	           "(:participante IS NULL OR i.participante = :participante) AND " +
	           "(:data IS NULL OR i.dataInscricao = :data) AND " +
	           "(:status IS NULL OR i.statusGeral = :status)")
	    List<Inscricao> buscarComFiltrosDinamicos(
	            @Param("evento") Evento evento, 
	            @Param("participante") Participante participante, 
	            @Param("data") LocalDate data,  
	            @Param("status") StatusGeral status
	    );
}