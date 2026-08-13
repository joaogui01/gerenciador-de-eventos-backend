package Projeto.Gerenciador_Eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Projeto.Gerenciador_Eventos.entity.Inscricao;
import Projeto.Gerenciador_Eventos.entity.Ticket;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;

public interface TicketRepository extends JpaRepository<Ticket, Long>{

	Ticket findByCodigoHashTicket(String codigoHashTicket);

	@Query("SELECT t FROM Ticket t WHERE " +
	           "(:inscricao IS NULL OR t.inscricao = :inscricao) AND " +
	           "(:codigo IS NULL OR t.codigoHashTicket = :codigo) AND " +
	           "(:status IS NULL OR t.statusGeral = :status)")
	    List<Ticket> buscarComFiltrosDinamicos(
	            @Param("inscricao") Inscricao inscricao, 
	            @Param("codigo") String codigoHashTicket,  
	            @Param("status") StatusGeral status
	    );
}