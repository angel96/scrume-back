package com.spring.API;

import javax.servlet.http.HttpSession;

import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class UserAccountApiController extends AbstractApiController {

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

}
