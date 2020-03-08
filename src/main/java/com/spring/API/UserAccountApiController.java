package com.spring.API;

import javax.servlet.http.HttpSession;

import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping("/api/login")
public class UserAccountApiController extends AbstractApiController {

	@GetMapping("/roles")
	@ResponseBody
	public ResponseEntity<?> findAllRoles() {
		ResponseEntity<?> result = null;

		try {
			Gson gson = new Gson();
			result = new ResponseEntity<>(gson.toJson(Role.values()), HttpStatus.OK);
			super.logger.info("/api/list" + HttpStatus.OK);
		} catch (final Throwable oops) {
			result = new ResponseEntity<>("Please, use a valid account", HttpStatus.UNAUTHORIZED);
			super.logger.error("GET /api/list " + HttpStatus.UNAUTHORIZED);
		}

		return result;
	}

	@GetMapping("/logout")
	@ResponseBody
	public ResponseEntity<?> logout(HttpSession session) {

		ResponseEntity<?> result = null;

		try {
			super.authenticateOrUnauthenticate(null);
			session.invalidate();
			super.logger.info("/api/profile/logout " + HttpStatus.OK);
		} catch (final Throwable oops) {
			super.logger.error(oops);
			result = new ResponseEntity<>("No esta autorizado", HttpStatus.UNAUTHORIZED);
			super.logger.error("GET /api/list" + HttpStatus.UNAUTHORIZED);
		}

		return result;
	}

}
