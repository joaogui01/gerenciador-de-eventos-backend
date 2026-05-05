package Projeto.Gerenciador_Eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Projeto.Gerenciador_Eventos.entity.Evento;

public interface EventoRepository extends JpaRepository<Evento, Integer>{
	List<Evento> findAllByIdStatus(Integer idStatus);
}
