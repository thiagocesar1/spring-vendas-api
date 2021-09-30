package io.github.thiagocesar1.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.thiagocesar1.api.security.jwt.JwtAuthFilter;
import io.github.thiagocesar1.api.security.jwt.JwtService;
import io.github.thiagocesar1.api.service.impl.UsuarioServiceImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	@Autowired
	private JwtService jwtService;
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public OncePerRequestFilter jwtFiler() {
		return new JwtAuthFilter(jwtService, usuarioService);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService)
			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/api/produtos/**").hasRole("ADMIN")
			.antMatchers("/api/clientes/**").hasAnyRole("USER", "ADMIN")
			.antMatchers("/api/pedidos/**").hasAnyRole("USER", "ADMIN")
			.antMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
			.antMatchers(HttpMethod.POST, "/api/usuarios/auth").permitAll()
			.anyRequest().authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilterBefore(jwtFiler(), UsernamePasswordAuthenticationFilter.class);
	}
}
