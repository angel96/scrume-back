package com.spring.Security;

import java.time.LocalDateTime;
import java.util.Base64;

import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.modelmapper.ModelMapper;
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

import com.spring.CustomObject.UserAccountDto;
import com.spring.Model.Sprint;
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
	
	public UserAccountDto save(UserAccountDto userAccountDto) {
		ModelMapper mapper = new ModelMapper();
		UserAccount userAccountEntity = mapper.map(userAccountDto, UserAccount.class);
		UserAccount userAccountDB = new UserAccount();
		this.validationUsername(userAccountEntity.getUsername());
		userAccountDB.setUsername(userAccountEntity.getUsername());
		this.validationPassword(userAccountEntity.getPassword());
		String password = Utiles.encryptedPassword(userAccountEntity.getPassword());
		userAccountDB.setPassword(password);
		userAccountDB.setCreatedAt(LocalDateTime.now());
		userAccountDB.setLastPasswordChangeAt(LocalDateTime.now());
		userAccountDB.setRoles(userAccountEntity.getRoles());
		this.repository.save(userAccountDB);
		UserAccountDto userAccountDtoBack = mapper.map(userAccountDB, UserAccountDto.class);
		userAccountDtoBack.setConfirmation(userAccountDto.getConfirmation());
		validateConfirmation(userAccountDtoBack.getConfirmation());
		return userAccountDtoBack;
	}
	
	public UserAccountDto update(Integer idUserAccount, UserAccountDto userAccountDto) {
		ModelMapper mapper = new ModelMapper();
		UserAccount userAccountEntity = mapper.map(userAccountDto, UserAccount.class);
		UserAccount userAccountDB = this.findOne(idUserAccount);
		this.validationUsername(userAccountEntity.getUsername());
		userAccountDB.setUsername(userAccountEntity.getUsername());
		this.validationPassword(userAccountEntity.getPassword());
		if (Utiles.matchesPassword(userAccountEntity.getPassword(), userAccountEntity.getPassword()) == false) {
			userAccountDB.setPassword(Utiles.encryptedPassword(userAccountEntity.getPassword()));
			userAccountDB.setLastPasswordChangeAt(LocalDateTime.now());
		} else {
			userAccountDB.setPassword(userAccountEntity.getPassword());
			userAccountDB.setLastPasswordChangeAt(userAccountEntity.getLastPasswordChangeAt());
		}
		userAccountDB.setCreatedAt(userAccountEntity.getCreatedAt());
		this.repository.saveAndFlush(userAccountDB);
		UserAccountDto userAccountDtoBack = mapper.map(userAccountDB, UserAccountDto.class);
		userAccountDtoBack.setConfirmation(userAccountDto.getConfirmation());
		validateConfirmation(userAccountDtoBack.getConfirmation());
		return userAccountDtoBack;
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
	
	private String validationPassword(String s) {
		String pattern = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";
		if (s.matches(pattern) == true) {
			return s;
		} else {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "the password does not consist of 8 characters, one upper and one lower case");
		}
	}
	
	private String validationUsername(String s) {
		String pattern = "^([a-zA-Z0-9_!#$%&*+/=?{|}~^.-]+@[a-zA-Z0-9.-]+)|([\\\\w\\\\s]+<[a-zA-Z0-9_!#$%&*+/=?{|}~^.-]+@[a-zA-Z0-9.-]+>+)|([0-9a-zA-Z]([-.\\\\\\\\w]*[0-9a-zA-Z])+@)|([\\\\w\\\\s]+<[a-zA-Z0-9_!#$%&*+/=?`{|}~^.-]+@+>)$";
		if (s.matches(pattern) == true) {
			return s;
		} else {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "the username is not a valid email");
		}
	}
	
	private void validateConfirmation(Boolean confirmation) {
		if (confirmation == false) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "the user did not accept the terms and conditions");
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
