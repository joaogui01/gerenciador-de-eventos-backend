package Projeto.Gerenciador_Eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Projeto.Gerenciador_Eventos.entity.Participante;

public interface ParticipanteRepository extends JpaRepository<Participante, Integer> {
	List<Participante> findAllByIdStatus(Integer idStatus);
}
