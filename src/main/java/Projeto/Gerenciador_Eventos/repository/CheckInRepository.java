package Projeto.Gerenciador_Eventos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Projeto.Gerenciador_Eventos.entity.CheckIn;
import Projeto.Gerenciador_Eventos.entity.Ticket;
import Projeto.Gerenciador_Eventos.entity.enums.StatusCheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn, Long>{

	CheckIn findByTicket(Ticket ticket);

	@Query("SELECT c FROM CheckIn c WHERE " +
	           "(:ticket IS NULL OR c.ticket = :ticket) AND " +
	           "(:data IS NULL OR c.dataCheckIn = :data) AND " +
	           "(:status IS NULL OR c.statusCheckIn = :status)")
	    List<CheckIn> buscarComFiltrosDinamicos(
	            @Param("ticket") Ticket ticket, 
	            @Param("data") LocalDateTime data,  
	            @Param("status") StatusCheckIn status
	    );
}
