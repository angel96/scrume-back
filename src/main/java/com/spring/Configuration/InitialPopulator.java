package com.spring.Configuration;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.spring.Security.UserAccountRepository;

/**
 * 
 * Uncomment this annotation if you want to repopulate database. Please, take
 * into account you will have to include entities in order.
 *
 */

//@Component
public class InitialPopulator implements CommandLineRunner {

	protected final Logger logger = Logger.getLogger(InitialPopulator.class);

	@Autowired
	private UserAccountRepository repositoryAccount;

	@Override
	public void run(String... args) throws Exception {

	}

}
