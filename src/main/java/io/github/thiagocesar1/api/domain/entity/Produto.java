package io.github.thiagocesar1.api.domain.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column
	@NotEmpty(message = "Campo descrição é obrigatório.")
	private String descricao;
	
	@Column(name = "preco_unitario")
	@NotNull(message = "Campo preço é obrigatório.")
	private BigDecimal precoUnitario;
}
