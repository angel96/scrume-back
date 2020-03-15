package com.spring.Scrume;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.spring.Security.UserAccountService;


public class WorkspaceAPITest extends AbstractTest {

	@PostConstruct
	public void initBefore() {
		super.authenticateOrUnauthenticate("angdellun");
	}

	@Test
	public void listWorkspaces() throws Exception {

		super.mockMvc
				.perform(get("/api/profile/list").contentType(MediaType.APPLICATION_JSON)
						.with(user(UserAccountService.getPrincipal())))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].name", is("Perfil 1")));

	}

	@Test
	public void getWorkspace() throws Exception {

		super.mockMvc
				.perform(get("/api/profile/list").contentType(MediaType.APPLICATION_JSON)
						.with(user(UserAccountService.getPrincipal())))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].name", is("Perfil 1")));

	}

	@Test
	public void createWorkspace() throws Exception {

		super.mockMvc
				.perform(get("/api/profile/list").contentType(MediaType.APPLICATION_JSON)
						.with(user(UserAccountService.getPrincipal())))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].name", is("Perfil 1")));

	}

	@Test
	public void deleteWorkspace() throws Exception {

		super.mockMvc
				.perform(get("/api/profile/list").contentType(MediaType.APPLICATION_JSON)
						.with(user(UserAccountService.getPrincipal())))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].name", is("Perfil 1")));

	}

	@After
	public void initFinish() {
		super.authenticateOrUnauthenticate(null);
	}
}
