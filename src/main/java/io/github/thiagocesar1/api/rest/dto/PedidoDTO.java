package io.github.thiagocesar1.api.rest.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
	private Integer cliente;
	private BigDecimal total;
	private List<ItemPedidoDTO> itens;
}
