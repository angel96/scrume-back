package com.spring.JWT;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.UserLoginDto;
import com.spring.Model.Payment;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Service.PaymentService;
import com.spring.Utiles.Utiles;

@Service
@Transactional
public class JwtUserAccountService implements UserDetailsService {

	@Autowired
	private JwtUserAccountRepository repository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private JwtToken jwtToken;

	public UserAccount loadUserByUsername(String username) {
		return repository.findByEmail(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, username + " not found"));
	}

	public User findUserByUsername(String username) {
		return repository.findUserByUserName(username)
				.orElseThrow(() ->new ResponseStatusException(HttpStatus.BAD_REQUEST, username + " not found"));
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

			User user = this.repository.findUserByUserName(username)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized"));
			Payment payment = this.paymentService.findByUserAccount(user.getUserAccount());

			Map<String, Object> objects = new HashMap<>();

			objects.put("userLoginDto",
					new UserLoginDto(user.getId(), username, payment.getBox().getName(), payment.getExpiredDate()));

			response.setToken(jwtToken.generateToken(objects, account));
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Username or Password");
		}

		return response;
	}

	public JwtResponse generateToken(UserAccount account) {

		User user = this.repository.findUserByUserName(account.getUsername())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized"));
		Payment payment = this.paymentService.findByUserAccount(user.getUserAccount());

		Map<String, Object> objects = new HashMap<>();

		objects.put("userLoginDto", new UserLoginDto(user.getId(), account.getUsername(), payment.getBox().getName(),
				payment.getExpiredDate()));

		return new JwtResponse(jwtToken.generateToken(objects, account));
	}
}
