package Projeto.Gerenciador_Eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Projeto.Gerenciador_Eventos.entity.Notificacao;
import Projeto.Gerenciador_Eventos.entity.Usuario;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

	List<Notificacao> findByUsuarioOrderByDataHoraDesc(Usuario usuario);

	long countByUsuarioAndLidaFalse(Usuario usuario);

}
