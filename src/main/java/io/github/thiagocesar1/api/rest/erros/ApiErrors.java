package io.github.thiagocesar1.api.rest.erros;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class ApiErrors {
	@Getter
	private List<String> errors;
	
	public ApiErrors(String mensagemErro) {
		this.errors = Arrays.asList(mensagemErro);
	}

	public ApiErrors(List<String> errors) {
		super();
		this.errors = errors;
	}

}
