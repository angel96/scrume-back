package com.spring.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.spring.Model.Administrator;
import com.spring.Model.Profile;
import com.spring.Model.UserAccount;
import com.spring.Repository.AdministratorRepository;
import com.spring.Repository.ProfileRepository;
import com.spring.Security.Role;
import com.spring.Security.UserAccountRepository;
import com.spring.Utiles.Utiles;

/**
 * 
 * Uncomment this annotation if you want to repopulate database. 
 * Please, take into account you will have to include entities in order.
 *
 */

@Component
public class Populator implements CommandLineRunner {

	protected final Logger logger = Logger.getLogger(Populator.class);

	@Autowired
	private UserAccountRepository repositoryAccount;

	@Autowired
	private AdministratorRepository repositoryAdmin;

	@Autowired
	private ProfileRepository repositoryProfile;

	@Override
	public void run(String... args) throws Exception {

		repositoryProfile.deleteAll();
		repositoryAdmin.deleteAll();
		repositoryAccount.deleteAll();

		UserAccount account = repositoryAccount.save(new UserAccount("angdellun", Utiles.encryptedPassword("123456"),
				LocalDateTime.now(), LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		UserAccount account2 = repositoryAccount.save(new UserAccount("angdellun2", Utiles.encryptedPassword("1234562"),
				LocalDateTime.now(), LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		Administrator actor = new Administrator();
		actor.setUserAccount(account);
		Administrator actor_saved_1 = repositoryAdmin.save(actor);

		Administrator actor2 = new Administrator();
		actor2.setUserAccount(account2);
		Administrator actor_saved_2 = repositoryAdmin.save(actor2);

		Profile profile1 = repositoryProfile.save(new Profile("Perfil 1", actor));
		Profile profile2 = repositoryProfile.save(new Profile("Perfil 2", actor2));

	}

}
