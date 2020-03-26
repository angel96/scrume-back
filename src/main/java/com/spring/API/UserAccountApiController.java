package com.spring.API;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.UserAccountDto;
import com.spring.CustomObject.UserDto;
import com.spring.Security.UserAccountService;

@RestController
@RequestMapping("/api/login")
public class UserAccountApiController extends AbstractApiController {

	@Autowired
	private UserAccountService service;

	@GetMapping("/roles")
	public Role[] findAllRoles() {
		super.logger.info("GET /api/login/roles");
		return Role.values();
	}

	@GetMapping("/logout")
	public void logout(HttpSession session) {
		super.logger.info("GET /api/profile/logout ");
		super.authenticateOrUnauthenticate(null);
		session.invalidate();
	}
	
	@PostMapping
	public UserAccountDto save(@RequestBody UserAccountDto userAccountDto) {
		super.logger.info("POST /api/userAccount");
		return this.service.save(userAccountDto);
	}
	
	@PutMapping("/{idUserAccount}")
	public UserAccountDto update(@PathVariable Integer idUserAccount, @RequestBody UserAccountDto userAccountDto) {
		super.logger.info("UPDATE /api/userAccount");
		return this.service.update(idUserAccount, userAccountDto);
	}

	@GetMapping("/isAValidUser")
	public Boolean isAValidUser(HttpServletRequest request) {
		super.logger.info("GET /api/login/isAValidUser");
		String auth = request.getHeader("authorization");
		return service.isAValidUser(auth);
	}

}
