package Projeto.Gerenciador_Eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Projeto.Gerenciador_Eventos.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>{
	List<Ticket> findAllByIdStatus(Integer idStatus);
}
