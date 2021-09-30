package io.github.thiagocesar1.api.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.thiagocesar1.api.domain.entity.Usuario;
import io.github.thiagocesar1.api.domain.repository.UsuarioRepository;
import io.github.thiagocesar1.api.exception.SenhaInvalidaException;

@Service
public class UsuarioServiceImpl implements UserDetailsService{
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByLogin(username)
						 .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
		String[] roles = usuario.isAdmin() ? new String[] {"ADMIN", "USER"} : new String[] {"USER"};
		return User.builder()
				   .username(usuario.getLogin())
				   .password(usuario.getSenha())
				   .roles(roles)
				   .build();
	}
	
	public UserDetails autenticar(Usuario usuario) {
		UserDetails usuarioDetail = loadUserByUsername(usuario.getLogin());
		boolean senhaCorreta = encoder.matches(usuario.getSenha(), usuarioDetail.getPassword());
		
		if(senhaCorreta){
			return usuarioDetail;
		}
		
		throw new SenhaInvalidaException();
	}

}
