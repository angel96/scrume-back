package com.spring.Security;

import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.Model.UserAccount;

@Service
@Transactional
public class UserAccountService implements UserDetailsService {

	@Autowired
	private UserAccountRepository repository;

	protected final Logger logger = Logger.getLogger(UserAccountService.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " no encontrado"));
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

}
