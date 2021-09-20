package io.github.thiagocesar1.api.domain.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.thiagocesar1.api.domain.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	List<Cliente> findByNomeLike(String nome);
	
	boolean existsByNome(String nome);
	
	@Query(value = "SELECT c FROM Cliente c LEFT JOIN fetch c.pedidos WHERE c.id = :idCliente")
	Cliente findClienteFetchPedidos(@Param("idCliente") Integer idCliente);
	
	
}
