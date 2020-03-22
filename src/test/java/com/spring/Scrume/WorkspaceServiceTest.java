package com.spring.Scrume;

import static org.junit.Assert.assertTrue;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Test;

public class WorkspaceServiceTest extends AbstractTest {

	@PostConstruct
	public void initBefore() {
		super.authenticateOrUnauthenticate("angdellun@gmail.com");
	}

	@Test
	public void workspaceDriverCreateTest() {
		assertTrue(true);
	}

	protected void driverWorkspaceDriverCreateTest() {

	}

	@After
	public void initFinish() {
		super.authenticateOrUnauthenticate(null);
	}
}
