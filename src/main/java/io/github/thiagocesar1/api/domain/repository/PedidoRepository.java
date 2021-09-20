package io.github.thiagocesar1.api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.thiagocesar1.api.domain.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
}
