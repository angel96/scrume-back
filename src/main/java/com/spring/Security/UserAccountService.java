package com.spring.Security;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.RegisterDto;
import com.spring.Model.UserAccount;
import com.spring.Utiles.Utiles;

@Service
@Transactional
public class UserAccountService implements UserDetailsService {

	@Autowired
	private UserAccountRepository repository;

	protected final Logger logger = Logger.getLogger(UserAccountService.class);

	@Override
	public UserDetails loadUserByUsername(String username) {
		return repository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " no encontrado"));
	}
	
	public UserAccount findOne(Integer idUserAccount) {
		return repository.findById(idUserAccount).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested userAccount doesn´t exists"));
	}
	
	public UserAccount save(UserAccount userAccount) {
		UserAccount userAccountEntity = new UserAccount();
		this.validationUsername(userAccount.getUsername());
		userAccountEntity.setUsername(userAccount.getUsername());
		this.validationPassword(userAccount.getPassword());
		String password = Utiles.encryptedPassword(userAccount.getPassword());
		userAccountEntity.setPassword(password);
		userAccountEntity.setCreatedAt(userAccount.getCreatedAt());
		userAccountEntity.setLastPasswordChangeAt(userAccount.getLastPasswordChangeAt());
		userAccountEntity.setRoles(userAccount.getRoles());
		this.repository.save(userAccountEntity);
		return userAccountEntity;
		
	}
	
	public UserAccount update(Integer idUserAccount, RegisterDto registerDto) {
		UserAccount userAccountEntity = this.findOne(idUserAccount);
		this.validationUsername(registerDto.getUsername());
		userAccountEntity.setUsername(registerDto.getUsername());
		this.validationPassword(registerDto.getPassword());
		if (Utiles.matchesPassword(registerDto.getPassword(), userAccountEntity.getPassword()) == false) {
			userAccountEntity.setPassword(Utiles.encryptedPassword(registerDto.getPassword()));
			userAccountEntity.setLastPasswordChangeAt(LocalDateTime.now());
		} else {
			userAccountEntity.setPassword(registerDto.getPassword());
			userAccountEntity.setLastPasswordChangeAt(registerDto.getLastPasswordChangeAt());
		}
		userAccountEntity.setCreatedAt(registerDto.getCreatedAt());
		userAccountEntity.setRoles(null);
		userAccountEntity.setId(idUserAccount);
		return this.repository.saveAndFlush(userAccountEntity);
	}

	public static UserAccount getPrincipal() {
		UserAccount result;
		SecurityContext context;
		Authentication authentication;
		Object principal;
		context = SecurityContextHolder.getContext();
		assert context != null;
		authentication = context.getAuthentication();
		assert authentication != null;
		principal = authentication.getPrincipal();
		assert principal instanceof UserAccount;
		result = (UserAccount) principal;
		assert result != null;
		assert result.getId() != 0;
		return result;
	}
	
	public String validationPassword(String s) {
		String pattern = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";
		if (s.matches(pattern) == true) {
			return s;
		} else {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "the password does not consist of 8 characters, one upper and one lower case");
		}
	}
	
	public String validationUsername(String s) {
		String pattern = "^([a-zA-Z0-9_!#$%&*+/=?{|}~^.-]+@[a-zA-Z0-9.-]+)|([\\\\w\\\\s]+<[a-zA-Z0-9_!#$%&*+/=?{|}~^.-]+@[a-zA-Z0-9.-]+>+)|([0-9a-zA-Z]([-.\\\\\\\\w]*[0-9a-zA-Z])+@)|([\\\\w\\\\s]+<[a-zA-Z0-9_!#$%&*+/=?`{|}~^.-]+@+>)$";
		if (s.matches(pattern) == true) {
			return s;
		} else {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "the username is not a valid email");
		}
	}

	public Boolean isAValidUser(String string) {
		System.out.println(string);
		Boolean res;
		Base64.Decoder dec = Base64.getDecoder();

		try {
			String auth = string.split(" ")[1];

			String decodedAuth = new String(dec.decode(auth));

			String username = decodedAuth.split(":")[0];
			String password = decodedAuth.split(":")[1];
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			res = encoder.matches(password, this.loadUserByUsername(username).getPassword());
		} catch (Exception e) {
			res = false;
		}
		return res;
	}

}
