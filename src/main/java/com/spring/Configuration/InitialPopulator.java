package com.spring.configuration;

import org.jboss.logging.Logger;
import org.springframework.boot.CommandLineRunner;

/**
 * 
 * Uncomment this annotation if you want to repopulate database. Please, take
 * into account you will have to include entities in order.
 *
 */

//@Component
public class InitialPopulator implements CommandLineRunner {

	protected final Logger logger = Logger.getLogger(InitialPopulator.class);

	public void run(String... args) throws Exception {
		// Waiting for initial data
	}

}
