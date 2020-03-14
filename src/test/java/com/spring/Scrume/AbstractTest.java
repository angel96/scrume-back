package com.spring.Scrume;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import com.spring.Configuration.H2Testing;
import com.spring.Security.UserAccountService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ScrumeApplication.class, H2Testing.class })
public abstract class AbstractTest {

	@Autowired
	private UserAccountService service;

	
	public void authenticateOrUnauthenticate(String username) {
		UserDetails userDetails = username == null ? null : service.loadUserByUsername(username);
		Authentication authenticationToken = new TestingAuthenticationToken(userDetails, null);
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authenticationToken);
	}

}
