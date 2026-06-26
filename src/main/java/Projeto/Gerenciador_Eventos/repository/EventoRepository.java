package Projeto.Gerenciador_Eventos.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Projeto.Gerenciador_Eventos.entity.Evento;
import Projeto.Gerenciador_Eventos.entity.enums.StatusGeral;

public interface EventoRepository extends JpaRepository<Evento, Long>{
	@Query("SELECT e FROM Evento e WHERE " +
	           "(:nome IS NULL OR LOWER(e.nomeEvento) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
	           "(:descricao IS NULL OR e.descricaoEvento = :descricao) AND " +
	           "(:data IS NULL OR e.dataEvento = :data) AND " +
	           "(:local IS NULL OR e.localEvento = :local) AND " +
	           "(:vagastotais IS NULL OR e.vagasTotaisEvento = :vagastotais) AND " +
	           "(:vagasdisponiveis IS NULL OR e.vagasDisponiveisEvento = :vagasdisponiveis) AND " +
	           "(:preco IS NULL OR e.precoEvento = :preco) AND " +
	           "(:status IS NULL OR e.statusGeral = :status)")
	    List<Evento> buscarComFiltrosDinamicos(
	            @Param("nome") String nome, 
	            @Param("descricao") String descricao, 
	            @Param("data") LocalDate data, 
	            @Param("local") String local, 
	            @Param("vagastotais") Integer vagasTotais,
	            @Param("vagasdisponiveis") Integer vagasDisponiveis,
	            @Param("preco") BigDecimal preco,
	            @Param("status") StatusGeral status
	    );
}
