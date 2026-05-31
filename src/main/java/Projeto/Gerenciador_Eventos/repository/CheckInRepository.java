package Projeto.Gerenciador_Eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Projeto.Gerenciador_Eventos.entity.CheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn, Long>{
	
	List<CheckInRepository> findAllByIdStatusCheckIn(Integer idStatusCheckIn);
}
