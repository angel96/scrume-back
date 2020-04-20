package com.spring.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtUserAccountService service;

	@Autowired
	private JwtRequestFilter filter;

	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPointBean() {
		return new JwtAuthenticationEntryPoint();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(passwordEncoder());
	}

	// For public urls
	@Override
	public void configure(WebSecurity web) {

		web.ignoring().antMatchers("/api/login/**");
		web.ignoring().antMatchers("/api/box/**");
		web.ignoring().antMatchers("/api/user/find-by-authorization");
		web.ignoring().antMatchers("/api/user/security-breach");
		web.ignoring().antMatchers("/api/login/authenticate");
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().and().httpBasic().and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests().and()
				.authorizeRequests().antMatchers("/api/team/*").authenticated().antMatchers("/api/sprint/*")
				.authenticated().antMatchers("/api/login/**").authenticated().antMatchers("/api/project/**")
				.authenticated().antMatchers("/api/workspace/**").authenticated().antMatchers("/api/history-task/**")
				.authenticated().antMatchers("/api/task/**").authenticated().antMatchers("/api/payment/**")
				.authenticated().antMatchers("/api/box/**").authenticated().antMatchers("/api/user/**").authenticated().antMatchers("/api/notification/**").authenticated()
				.antMatchers("/api/administrator/**").hasRole("ADMIN")
				.antMatchers("/api/document/**").authenticated().antMatchers("/api/note/**").authenticated();

		// Probar si al cierre de sesion, sigue disponible la API
		http.logout().logoutUrl("/api/login/logout").clearAuthentication(true).deleteCookies("JSESSIONID").and().csrf()
				.disable();

		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

	}

}
