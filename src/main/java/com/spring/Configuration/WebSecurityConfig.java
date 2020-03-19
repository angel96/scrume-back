package com.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.security.UserAccountService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomBasicAuthenticationPoint customBasicAuthenticationEntryPoint;
	private final UserAccountService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Probar si al cierre de sesion, sigue disponible la API
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.logout().logoutUrl("/api/login/logout").clearAuthentication(true).deleteCookies("JSESSIONID").and().csrf()
				.disable();

		http.cors().and().httpBasic().authenticationEntryPoint(customBasicAuthenticationEntryPoint).and()
				.authorizeRequests().antMatchers("/api/team/*").authenticated().antMatchers("/api/sprint/*").authenticated()
				.antMatchers("/api/login/roles").authenticated().antMatchers("/api/project/**").authenticated()
				.antMatchers("/api/workspace/**").authenticated().antMatchers("/api/history-task/**").authenticated()
				.antMatchers("/api/task/**").authenticated();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
