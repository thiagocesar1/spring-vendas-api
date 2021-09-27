package io.github.thiagocesar1.api.rest.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.github.thiagocesar1.api.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
	@NotNull(message = "Informe o código do Cliente")
	private Integer cliente;
	@NotNull(message = "Campo Total do pedido é obrigatório")
	private BigDecimal total;
	@NotEmptyList(message = "É necessário incluir pelo menos um produto no pedido.")
	private List<ItemPedidoDTO> itens;
}
