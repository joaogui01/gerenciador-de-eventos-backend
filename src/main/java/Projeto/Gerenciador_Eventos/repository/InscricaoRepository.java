package Projeto.Gerenciador_Eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Projeto.Gerenciador_Eventos.entity.Inscricao;

public interface InscricaoRepository extends JpaRepository<Inscricao, Integer> {
	List<Inscricao> findAllByIdStatus(Integer idStatus);
}
