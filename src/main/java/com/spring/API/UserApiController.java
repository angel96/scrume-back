package com.spring.API;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.UserOfATeamByWorspaceDto;
import com.spring.Service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserApiController extends AbstractApiController {

	@Autowired
	private UserService userService;

	@GetMapping("/list-by-workspace/{idWorkspace}")
	public Collection<UserOfATeamByWorspaceDto> listUsersOfATeamByWorkspace(@PathVariable int idWorkspace) {
		super.logger.info("GET /api/user/list-by-workspace/" + idWorkspace);
		return this.userService.listUsersOfATeamByWorkspace(idWorkspace);
	}

}
