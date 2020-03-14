package com.spring.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.spring.Model.Administrator;
import com.spring.Model.Box;
import com.spring.Model.Profile;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Repository.AdministratorRepository;
import com.spring.Repository.BoxRepository;
import com.spring.Repository.ProfileRepository;
import com.spring.Repository.UserRepository;
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
	
	@Autowired
	private BoxRepository boxRepository;
	
	@Autowired
	private UserRepository userRepository;
    
	@Autowired
	private ProjectRepository repositoryProject;
    
    @Autowired
    private TeamRepository repositoryTeam;
	
	@Override
	public void run(String... args) throws Exception {

		userRepository.deleteAll();
		boxRepository.deleteAll();
		repositoryProfile.deleteAll();
		repositoryAdmin.deleteAll();
		repositoryAccount.deleteAll();
        repositoryProject.deleteAll();

		UserAccount account = repositoryAccount.save(new UserAccount("angdellun@gmail.com", Utiles.encryptedPassword("123456"),
				LocalDateTime.now(), LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		UserAccount account2 = repositoryAccount.save(new UserAccount("angdellun2@gmail.com", Utiles.encryptedPassword("1234562"),
				LocalDateTime.now(), LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		UserAccount userAccount = repositoryAccount.save(new UserAccount("testuser@gmail.com", Utiles.encryptedPassword("123456"),
				LocalDateTime.now(), LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));
		
		Box basicBox = new Box();
		basicBox.setName("BASIC");
		basicBox.setPrice(0.0);
		boxRepository.save(basicBox);
		
		Box standardBox = new Box();
		standardBox.setName("STANDARD");
		standardBox.setPrice(1.0);
		boxRepository.save(standardBox);
		
		Box proBox = new Box();
		proBox.setName("PRO");
		proBox.setPrice(2.0);
		boxRepository.save(proBox);
		
		User user = new User();
		user.setBox(proBox);
		Date date = new GregorianCalendar(2020, Calendar.DECEMBER, 30).getTime();
		
		user.setEndingBoxDate(date);
		user.setName("Name");
		user.setNick("nick");
		user.setSurnames("surnames");
		user.setUserAccount(userAccount);
		userRepository.save(user);
		
		Administrator actor = new Administrator();
		actor.setUserAccount(account);
		Administrator actor_saved_1 = repositoryAdmin.save(actor);

		Administrator actor2 = new Administrator();
		actor2.setUserAccount(account2);
		Administrator actor_saved_2 = repositoryAdmin.save(actor2);

		Profile profile1 = repositoryProfile.save(new Profile("Perfil 1", actor));
		Profile profile2 = repositoryProfile.save(new Profile("Perfil 2", actor2));
        
        Team team1 = repositoryTeam.save(new Team("Equipo 1"));
		Team team2 = repositoryTeam.save(new Team("Equipo 2"));
		Project project1 = repositoryProject.save(new Project("Proyecto 1", "", team1));
		Project project2 = repositoryProject.save(new Project("Proyecto 2", "", team1));
		Project project3 = repositoryProject.save(new Project("Proyecto 3", "", team2));
		Project project4 = repositoryProject.save(new Project("Proyecto 4", "", team2));
		Project project5 = repositoryProject.save(new Project("Proyecto 5", "", team2));

	}

}	