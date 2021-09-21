package io.github.thiagocesar1.api.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.thiagocesar1.api.domain.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
	@Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.id = :id")
	Optional<Pedido> findByIdFetchItens(@Param("id") Integer id);
}
