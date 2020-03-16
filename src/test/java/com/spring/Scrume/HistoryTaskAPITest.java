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

public class HistoryTaskAPITest extends AbstractTest {

	@PostConstruct
	public void initBefore() {
		super.authenticateOrUnauthenticate("angdellun@gmail.com");
	}

	@After
	public void initFinish() {
		super.authenticateOrUnauthenticate(null);
	}
}
