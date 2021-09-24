package io.github.thiagocesar1.api.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.thiagocesar1.api.domain.entity.Cliente;
import io.github.thiagocesar1.api.domain.entity.ItemPedido;
import io.github.thiagocesar1.api.domain.entity.Pedido;
import io.github.thiagocesar1.api.domain.entity.Produto;
import io.github.thiagocesar1.api.domain.enums.StatusPedido;
import io.github.thiagocesar1.api.domain.repository.ClienteRepository;
import io.github.thiagocesar1.api.domain.repository.ItemPedidoRepository;
import io.github.thiagocesar1.api.domain.repository.PedidoRepository;
import io.github.thiagocesar1.api.domain.repository.ProdutoRepository;
import io.github.thiagocesar1.api.exception.RegraNegocioException;
import io.github.thiagocesar1.api.rest.dto.ItemPedidoDTO;
import io.github.thiagocesar1.api.rest.dto.PedidoDTO;
import io.github.thiagocesar1.api.rest.erros.PedidoNaoEncontradoException;
import io.github.thiagocesar1.api.service.PedidoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {
	private final PedidoRepository pedidoRepository;
	private final ClienteRepository clienteRepository;
	private final ProdutoRepository produtoRepository;
	private final ItemPedidoRepository itemPedidoRepository;


	@Override
	@Transactional
	public Pedido salvar(PedidoDTO pedidoDTO) {
		Integer idCliente = pedidoDTO.getCliente();
		Pedido pedido = new Pedido();
		Cliente cliente = clienteRepository.findById(idCliente)
						 .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido"));
						
		
		pedido.setTotal(pedidoDTO.getTotal());
		pedido.setDataPedido(LocalDate.now());
		pedido.setCliente(cliente);
		pedido.setStatus(StatusPedido.REALIZADO);
		
		List<ItemPedido> itensPedido = converterItens(pedido, pedidoDTO.getItens());
		pedidoRepository.save(pedido);
		itemPedidoRepository.saveAll(itensPedido);
		pedido.setItens(itensPedido);
		
		return pedido;
	}
	
	private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens){
		if(itens.isEmpty()) {
			throw new RegraNegocioException("Não é possível realizar um pedido sem itens.");
		}
		return itens.stream().map(dto -> {
			Integer idProduto = dto.getProduto();
			Produto produto = produtoRepository.findById(idProduto)
							 .orElseThrow(() -> new RegraNegocioException("Código de produto inválido: "+idProduto));
			
			ItemPedido itemPedido = new ItemPedido();
			itemPedido.setQuantitade(dto.getQuantidade());
			itemPedido.setProduto(produto);
			itemPedido.setPedido(pedido);
			
			return itemPedido;
		}).collect(Collectors.toList());
		
	}

	@Override
	public Optional<Pedido> obterPedidoCompleto(Integer id) {
		return pedidoRepository.findByIdFetchItens(id);
	}

	@Override
	@Transactional
	public void atualizaStatus(Integer id, StatusPedido statusPedido) {
		pedidoRepository.findById(id)
						.map(pedido -> {
							pedido.setStatus(statusPedido);
							return pedidoRepository.save(pedido);
						}).orElseThrow(() -> new PedidoNaoEncontradoException());
		
	}	
}
