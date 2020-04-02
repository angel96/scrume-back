package com.spring.JWT;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.Model.UserAccount;
import com.spring.Utiles.Utiles;

@Service
@Transactional
public class JwtUserAccountService implements UserDetailsService {

	@Autowired
	private JwtUserAccountRepository repository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtToken jwtToken;

	public UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " no encontrado"));
	}

	public JwtResponse generateToken(JwtRequest authenticationRequest) {

		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();

		if (username.equals("") || password.equals("")) {
			throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED,
					"Username and Password can not be empty");
		}

		UserAccount account = this.loadUserByUsername(username);

		boolean checkUser = account != null && Utiles.matchesPassword(password, account.getPassword());
		JwtResponse response = new JwtResponse();
		if (checkUser) {
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
			authenticationManager.authenticate(authentication);
			response.setToken(jwtToken.generateToken(account));
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Username or Password");
		}

		return response;
	}

}
