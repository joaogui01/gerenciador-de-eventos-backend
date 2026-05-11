package Projeto.Gerenciador_Eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Projeto.Gerenciador_Eventos.entity.StatusCheckIn;

public interface StatusCheckInRepository extends JpaRepository<StatusCheckIn, Integer>{

}
