package io.github.thiagocesar1.api.rest.controller;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.thiagocesar1.api.domain.entity.ItemPedido;
import io.github.thiagocesar1.api.domain.entity.Pedido;
import io.github.thiagocesar1.api.domain.enums.StatusPedido;
import io.github.thiagocesar1.api.rest.dto.AtualizacaoStatusPedidoDTO;
import io.github.thiagocesar1.api.rest.dto.InformacaoItemPedidoDTO;
import io.github.thiagocesar1.api.rest.dto.InformacoesPedidoDTO;
import io.github.thiagocesar1.api.rest.dto.PedidoDTO;
import io.github.thiagocesar1.api.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
	private PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Integer save(@RequestBody PedidoDTO pedidoDTO) {
		Pedido pedido = pedidoService.salvar(pedidoDTO);
		return pedido.getId();
	}
	
	@GetMapping("/{id}")
	public InformacoesPedidoDTO getById(@PathVariable Integer id) {
		return pedidoService.obterPedidoCompleto(id)
				            .map(p -> converter(p))
				            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));
	}
	
	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateStatus(@RequestBody AtualizacaoStatusPedidoDTO dto, @PathVariable Integer id) {
		pedidoService.atualizaStatus(id, StatusPedido.valueOf(dto.getNovoStatus()));
	}
	
	private InformacoesPedidoDTO converter(Pedido pedido) {
		return InformacoesPedidoDTO.builder()
							.codigo(pedido.getId())
							.dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
							.cpf(pedido.getCliente().getCpf())
							.nomeCliente(pedido.getCliente().getNome())
							.total(pedido.getTotal())
							.status(pedido.getStatus().name())
							.itens(converterItens(pedido.getItens()))
							.build();
	}
	
	private List<InformacaoItemPedidoDTO> converterItens(List<ItemPedido> itens){
		if(CollectionUtils.isEmpty(itens)) {
			return Collections.emptyList();
		}
		
		return itens.stream().map(item -> InformacaoItemPedidoDTO.builder()
																 .descricaoProduto(item.getProduto().getDescricao())
																 .precoUnitario(item.getProduto().getPrecoUnitario())
																 .quantidade(item.getQuantitade())
																 .build()
								 ).collect(Collectors.toList());
	}
}
