package io.github.thiagocesar1.api.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.thiagocesar1.api.domain.entity.Produto;
import io.github.thiagocesar1.api.domain.repository.ProdutoRepository;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController{
	private ProdutoRepository produtoRepository;

	public ProdutoController(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Produto save(@RequestBody @Valid Produto produto) {
		return produtoRepository.save(produto);
	}
	
	@GetMapping("/{id}")
	public Produto findById(@PathVariable Integer id) {
		return produtoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id){
		this.produtoRepository.findById(id)
				.map(produto -> {
					produtoRepository.delete(produto);
					return produto;
				}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable Integer id, @RequestBody @Valid Produto produto) {
		produtoRepository.findById(id)
							  .map(produtoExists -> {
								  produto.setId(produtoExists.getId());
								  produtoRepository.save(produto);
								  return produto;
							  }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
	}
	
	@GetMapping
	public List<Produto> findByFilters(Produto filters){
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING);
		
		Example<Produto> exampleProduto = Example.of(filters, matcher);
		
		return produtoRepository.findAll(exampleProduto);
	}
}






























