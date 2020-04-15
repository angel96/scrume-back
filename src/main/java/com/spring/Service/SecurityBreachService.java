package com.spring.Service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.SecurityBreachDto;
import com.spring.Model.SecurityBreach;
import com.spring.Model.User;
import com.spring.Repository.SecurityBreachRepository;
import com.spring.Security.Role;

@Service
@Transactional
public class SecurityBreachService extends AbstractService {

	@Autowired
	private SecurityBreachRepository securityBreachRepository;

	@Autowired
	private UserService userService;
	
	public SecurityBreach getSecurityBreach() {
		return this.securityBreachRepository.findAll().get(0);
	}

	public SecurityBreach updateSecurityBreach(SecurityBreachDto securityBreachDto) {
		User principal = this.userService.getUserByPrincipal();
		this.validateIsLogged(principal);
		this.validatePrincipal(principal);
		SecurityBreach securityBreachEntity = this.securityBreachRepository.findAll().get(0);
		securityBreachEntity.setMessage(securityBreachDto.getMessage());
		securityBreachEntity.setActivated(securityBreachDto.getActivated());
		return this.securityBreachRepository.saveAndFlush(securityBreachEntity);
	}
	
	private void validateIsLogged(User principal) {
		if(principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
					"The user must be logged in");
		}
	}
	private void validatePrincipal(User principal) {
		if(!principal.getUserAccount().getRoles().contains(Role.ROLE_ADMIN)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
				"The user does not have permission to manage the security breach");
		}
	}

	public void flush() {
		securityBreachRepository.flush();
	}
}
