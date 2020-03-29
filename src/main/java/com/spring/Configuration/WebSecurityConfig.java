package com.spring.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.Security.UserAccountService;

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

	// For private access
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// No session will be created or used by spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.cors().and().httpBasic().and().exceptionHandling()
				.authenticationEntryPoint(customBasicAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests().and()
				.authorizeRequests().antMatchers("/api/team/*").authenticated().antMatchers("/api/sprint/*")
				.authenticated().antMatchers("/api/login/**").authenticated().antMatchers("/api/project/**")
				.authenticated().antMatchers("/api/workspace/**").authenticated().antMatchers("/api/history-task/**")
				.authenticated().antMatchers("/api/task/**").authenticated().antMatchers("/api/payment/**")
				.authenticated().antMatchers("/api/box/**").authenticated().antMatchers("/api/user/**").authenticated()
				.antMatchers("/api/document/**").authenticated();

		// Probar si al cierre de sesion, sigue disponible la API
		http.logout().logoutUrl("/api/login/logout").clearAuthentication(true).deleteCookies("JSESSIONID").and().csrf()
				.disable();

	}

	// For public urls
	@Override
	public void configure(WebSecurity web) {
		
		web.ignoring().antMatchers("/api/login/**");
		web.ignoring().antMatchers("/api/box/**");
		web.ignoring().antMatchers("/api/user/find-by-authorization");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
