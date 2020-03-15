package com.spring.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.spring.Model.Administrator;
import com.spring.Model.Box;
import com.spring.Model.Column;
import com.spring.Model.Profile;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Model.UserRol;
import com.spring.Model.Workspace;
import com.spring.Repository.AdministratorRepository;
import com.spring.Repository.BoxRepository;
import com.spring.Repository.ColumnRepository;
import com.spring.Repository.ProfileRepository;
import com.spring.Repository.ProjectRepository;
import com.spring.Repository.SprintRepository;
import com.spring.Repository.TeamRepository;
import com.spring.Repository.UserRepository;
import com.spring.Repository.UserRolRepository;
import com.spring.Repository.WorkspaceRepository;
import com.spring.Security.Role;
import com.spring.Security.UserAccountRepository;
import com.spring.Utiles.Utiles;

/**
 * 
 * Uncomment this annotation if you want to repopulate database. Please, take
 * into account you will have to include entities in order.
 *
 */

//@Component
public class PopulatorDatabase implements CommandLineRunner {

	protected final Logger logger = Logger.getLogger(PopulatorDatabase.class);

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

	@Autowired
	private SprintRepository repositorySprint;

	@Autowired
	private WorkspaceRepository repositoryWorkspace;

	@Autowired
	private ColumnRepository repositoryColumn;

	@Autowired
	private UserRolRepository repositoryUserRol;

	@Override
	public void run(String... args) throws Exception {

		repositoryUserRol.deleteAll();
		repositoryColumn.deleteAll();
		repositoryWorkspace.deleteAll();
		repositorySprint.deleteAll();
		repositoryProject.deleteAll();
		repositoryTeam.deleteAll();
		userRepository.deleteAll();
		boxRepository.deleteAll();
		repositoryProfile.deleteAll();
		repositoryAdmin.deleteAll();
		repositoryAccount.deleteAll();
		

		UserAccount account = repositoryAccount
				.save(new UserAccount("angdellun@gmail.com", Utiles.encryptedPassword("123456"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		UserAccount account2 = repositoryAccount
				.save(new UserAccount("angdellun2@gmail.com", Utiles.encryptedPassword("1234562"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		UserAccount userAccount = repositoryAccount
				.save(new UserAccount("testuser@gmail.com", Utiles.encryptedPassword("123456"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

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

		UserRol rol1 = this.repositoryUserRol.save(new UserRol(true, user, team1));

		Project project1 = repositoryProject.save(new Project("Proyecto 1", "Proyecto 1", team1));
		Project project2 = repositoryProject.save(new Project("Proyecto 2", "Proyecto 2", team1));
		Project project3 = repositoryProject.save(new Project("Proyecto 3", "Proyecto 3", team2));
		Project project4 = repositoryProject.save(new Project("Proyecto 4", "Proyecto 4", team2));
		Project project5 = repositoryProject.save(new Project("Proyecto 5", "Proyecto 5", team2));

		LocalDateTime localDateTime = LocalDateTime.now();
		Date localDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		LocalDateTime localDateTime1 = LocalDateTime.of(2020, 04, 03, 10, 15);
		Date localDate1 = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		Sprint sprint1 = this.repositorySprint.save(new Sprint(localDate, localDate1, project1));

		Workspace workspace1 = this.repositoryWorkspace.save(new Workspace("Este es el workspace de prueba", sprint1));

		Column toDo = new Column("To Do", workspace1);
		Column inProgress = new Column("In progress", workspace1);
		Column done = new Column("Done", workspace1);

		List<Column> saveTo = repositoryColumn.saveAll(Arrays.asList(toDo, inProgress, done));
	}

}
