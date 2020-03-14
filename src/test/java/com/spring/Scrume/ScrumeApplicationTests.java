package com.spring.Scrume;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.spring.Configuration.H2Testing;
import com.spring.Security.UserAccountService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ScrumeApplication.class, H2Testing.class })
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public class ScrumeApplicationTests extends AbstractTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void test() throws Exception {
		super.authenticateOrUnauthenticate("angdellun");
		mockMvc.perform(get("/api/profile/list").contentType(MediaType.APPLICATION_JSON)
				.with(user(UserAccountService.getPrincipal()))).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name", is("Perfil 1")));
		super.authenticateOrUnauthenticate(null);
	}

}
