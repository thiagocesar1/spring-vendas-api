package io.github.thiagocesar1.api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.thiagocesar1.api.domain.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

}
