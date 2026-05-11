package Projeto.Gerenciador_Eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Projeto.Gerenciador_Eventos.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Integer>{

}
