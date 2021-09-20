package io.github.thiagocesar1.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import io.github.thiagocesar1.api.domain.entity.Cliente;
import io.github.thiagocesar1.api.domain.repository.ClienteRepository;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
	
	private ClienteRepository clienteRepository;
	
	public ClienteController(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}
	
	@GetMapping("/{id}")
	public Cliente getClienteById(@PathVariable Integer id) {
		return clienteRepository.findById(id)
			   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente save(@RequestBody Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		clienteRepository.findById(id)
						 .map(cliente -> {
							 clienteRepository.delete(cliente);
							 return cliente;
						 })
						 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));
		
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable Integer id, @RequestBody Cliente cliente) {
		clienteRepository
					.findById(id)
					.map(clienteExistente -> {
						cliente.setId(clienteExistente.getId());
						clienteRepository.save(cliente);
						return clienteExistente;
					})
					 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));
	}
	
	
	@GetMapping
	public List<Cliente> find(Cliente filtro){
		ExampleMatcher matcher = ExampleMatcher
										.matching()
										.withIgnoreCase()
										.withStringMatcher(StringMatcher.CONTAINING);
		
		Example<Cliente> example = Example.of(filtro, matcher);
		return clienteRepository.findAll(example);
	}
}
