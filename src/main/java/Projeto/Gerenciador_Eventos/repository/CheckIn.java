package Projeto.Gerenciador_Eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckIn extends JpaRepository<CheckIn, Long>{
	
	List<CheckIn> findAllByIdStatusCheckIn(Integer idStatusCheckIn);
}
