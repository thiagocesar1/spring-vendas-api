package io.github.thiagocesar1.api.rest.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.thiagocesar1.api.domain.entity.Usuario;
import io.github.thiagocesar1.api.exception.SenhaInvalidaException;
import io.github.thiagocesar1.api.rest.dto.CredenciaisDTO;
import io.github.thiagocesar1.api.rest.dto.TokenDTO;
import io.github.thiagocesar1.api.security.jwt.JwtService;
import io.github.thiagocesar1.api.service.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
	
	private final UsuarioServiceImpl usuarioService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario salvar(@RequestBody @Valid Usuario usuario) {
		String senhaCripto = passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCripto);
		return usuarioService.salvar(usuario);
	}
	
	@PostMapping("/auth")
	public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
		try {
			Usuario usuario = Usuario.builder().login(credenciais.getLogin()).senha(credenciais.getSenha()).build();
			UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
			String token = jwtService.gerarToken(usuario);
			
			return new TokenDTO(token, usuario.getLogin());
		}catch(UsernameNotFoundException | SenhaInvalidaException ex) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
		}
	}
}
