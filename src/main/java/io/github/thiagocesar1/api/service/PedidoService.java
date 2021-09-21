package io.github.thiagocesar1.api.service;

import java.util.Optional;

import io.github.thiagocesar1.api.domain.entity.Pedido;
import io.github.thiagocesar1.api.rest.dto.PedidoDTO;

public interface PedidoService {
	Pedido salvar(PedidoDTO pedidoDTO);
	Optional<Pedido> obterPedidoCompleto(Integer id);
}
