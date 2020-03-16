package com.spring.Scrume;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.Model.UserAccount;
import com.spring.Security.UserAccountRepository;

public class HistoryTaskAPITest extends AbstractTest {

	@Autowired
	private UserAccountRepository repository;
	
	@PostConstruct
	public void initBefore() {
		super.authenticateOrUnauthenticate("angdellun@gmail.com");
	}

	@Test
	public void prueba() {
		UserAccount user = repository.getOne(super.entities().get("account"));
		System.out.println(user.getId());
		assert user != null;
	}
	
	@After
	public void initFinish() {
		super.authenticateOrUnauthenticate(null);
	}
}
