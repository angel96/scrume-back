package com.spring.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
import com.spring.Model.Task;
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

@Component
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
		
		UserAccount account3 = repositoryAccount
				.save(new UserAccount("testuser2@gmail.com", Utiles.encryptedPassword("123456"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));
		
		UserAccount account4 = repositoryAccount
				.save(new UserAccount("testuser3@gmail.com", Utiles.encryptedPassword("123456"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));
		
		UserAccount account5 = repositoryAccount
				.save(new UserAccount("testuser4@gmail.com", Utiles.encryptedPassword("123456"), LocalDateTime.now(),
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
		
		User user2 = new User();
		user2.setBox(basicBox);
		Date date2 = new GregorianCalendar(2020, Calendar.DECEMBER, 29).getTime();
		
		User user3 = new User();
		user3.setBox(basicBox);
		Date date3 = new GregorianCalendar(2020, Calendar.DECEMBER, 28).getTime();
		

		user.setEndingBoxDate(date);
		user.setName("Name");
		user.setNick("nick");
		user.setSurnames("surnames");
		user.setUserAccount(userAccount);
		userRepository.save(user);
		
		user2.setEndingBoxDate(date2);
		user2.setName("Name2");
		user2.setNick("nick2");
		user2.setSurnames("surnames2");
		user2.setUserAccount(account2);
		userRepository.save(user2);
		
		user3.setEndingBoxDate(date3);
		user3.setName("Name3");
		user3.setNick("nick3");
		user3.setSurnames("surnames3");
		user3.setUserAccount(account3);
		userRepository.save(user3);

//		Administrator actor = new Administrator();
//		actor.setUserAccount(account);
//		Administrator actor_saved_1 = repositoryAdmin.save(actor);
//
//		Administrator actor2 = new Administrator();
//		actor2.setUserAccount(account2);
//		Administrator actor_saved_2 = repositoryAdmin.save(actor2);
//		
//		Administrator actor3 = new Administrator();
//		actor3.setUserAccount(account3);
//		Administrator actor_saved_3 = repositoryAdmin.save(actor3);
//		
//		Administrator actor4 = new Administrator();
//		actor4.setUserAccount(account4);
//		Administrator actor_saved_4 = repositoryAdmin.save(actor4);
//		
//		Administrator actor5 = new Administrator();
//		actor5.setUserAccount(account5);
//		Administrator actor_saved_5 = repositoryAdmin.save(actor5);
//
//		Profile profile1 = repositoryProfile.save(new Profile("Perfil 1", actor));
//		Profile profile2 = repositoryProfile.save(new Profile("Perfil 2", actor2));
//		Profile profile3 = repositoryProfile.save(new Profile("Perfil 3", actor3));
//		Profile profile4 = repositoryProfile.save(new Profile("Perfil 4", actor4));
//		Profile profile5 = repositoryProfile.save(new Profile("Perfil 5", actor5));

		Team team1 = repositoryTeam.save(new Team("Equipo 1"));
		Team team2 = repositoryTeam.save(new Team("Equipo 2"));
		Team team3 = repositoryTeam.save(new Team("Equipo 3"));
		Team team4 = repositoryTeam.save(new Team("Equipo 4"));
		Team team5 = repositoryTeam.save(new Team("Equipo 5"));

		UserRol rol1 = this.repositoryUserRol.save(new UserRol(true, user, team1));
		UserRol rol2 = this.repositoryUserRol.save(new UserRol(true, user2, team2));
		UserRol rol3 = this.repositoryUserRol.save(new UserRol(true, user3, team3));

		Project project1 = repositoryProject.save(new Project("Proyecto 1", "Proyecto 1", team1));
		Project project2 = repositoryProject.save(new Project("Proyecto 2", "Proyecto 2", team1));
		Project project3 = repositoryProject.save(new Project("Proyecto 3", "Proyecto 3", team2));
		Project project4 = repositoryProject.save(new Project("Proyecto 4", "Proyecto 4", team2));
		Project project5 = repositoryProject.save(new Project("Proyecto 5", "Proyecto 5", team2));

		LocalDateTime localDateTime = LocalDateTime.now();
		Date localDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime1 = LocalDateTime.of(2020, 4, 03, 10, 15);
		Date localDate1 = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime2 = LocalDateTime.of(2020, 5, 13, 10, 15);
		Date localDate2 = Date.from(localDateTime2.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime3 = LocalDateTime.of(2020, 6, 24, 10, 15);
		Date localDate3 = Date.from(localDateTime3.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime4 = LocalDateTime.of(2020, 7, 05, 10, 15);
		Date localDate4 = Date.from(localDateTime4.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime5 = LocalDateTime.of(2020, 8, 25, 10, 15);
		Date localDate5 = Date.from(localDateTime5.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime6 = LocalDateTime.of(2020, 9, 05, 10, 15);
		Date localDate6 = Date.from(localDateTime6.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime7 = LocalDateTime.of(2020, 10, 25, 10, 15);
		Date localDate7 = Date.from(localDateTime7.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime8 = LocalDateTime.of(2020, 7, 05, 10, 15);
		Date localDate8 = Date.from(localDateTime8.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime9 = LocalDateTime.of(2020, 7, 25, 10, 15);
		Date localDate9 = Date.from(localDateTime9.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime10 = LocalDateTime.of(2020, 8, 25, 10, 15);
		Date localDate10 = Date.from(localDateTime10.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime11 = LocalDateTime.of(2020, 8, 25, 10, 15);
		Date localDate11 = Date.from(localDateTime11.atZone(ZoneId.systemDefault()).toInstant());

		Sprint sprint1 = this.repositorySprint.save(new Sprint(localDate, localDate1, project1));
		Sprint sprint2 = this.repositorySprint.save(new Sprint(localDate2, localDate3, project1));
		Sprint sprint3 = this.repositorySprint.save(new Sprint(localDate4, localDate5, project1));
		Sprint sprint4 = this.repositorySprint.save(new Sprint(localDate6, localDate7, project2));
		Sprint sprint5 = this.repositorySprint.save(new Sprint(localDate8, localDate9, project2));
		Sprint sprint6 = this.repositorySprint.save(new Sprint(localDate10, localDate11, project2));
			

		Workspace workspace1 = this.repositoryWorkspace.save(new Workspace("Este es el workspace de prueba", sprint1));
		Workspace workspace2 = this.repositoryWorkspace.save(new Workspace("Workspace 2", sprint2));
		Workspace workspace3 = this.repositoryWorkspace.save(new Workspace("Workspace 3", sprint3));
		Workspace workspace4 = this.repositoryWorkspace.save(new Workspace("Workspace 4", sprint4));
		Workspace workspace5 = this.repositoryWorkspace.save(new Workspace("Workspace 5", sprint5));
		Workspace workspace6 = this.repositoryWorkspace.save(new Workspace("Workspace 6", sprint6));

		Column toDo = new Column("To Do", workspace1);
		Column inProgress = new Column("In progress", workspace1);
		Column done = new Column("Done", workspace1);
		Column toDo2 = new Column("To Do", workspace2);
		Column inProgress2 = new Column("In progress", workspace2);
		Column done2 = new Column("Done", workspace2);
		Column toDo3 = new Column("To Do", workspace3);
		Column inProgress3 = new Column("In progress", workspace3);
		Column done3 = new Column("Done", workspace3);
		Column toDo4 = new Column("To Do", workspace4);
		Column inProgress4 = new Column("In progress", workspace4);
		Column done4 = new Column("Done", workspace4);
		Column toDo5 = new Column("To Do", workspace5);
		Column inProgress5 = new Column("In progress", workspace5);
		Column done5 = new Column("Done", workspace5);
		Column toDo6 = new Column("To Do", workspace6);
		Column inProgress6 = new Column("In progress", workspace6);
		Column done6 = new Column("Done", workspace6);

		List<Column> saveTo = repositoryColumn.saveAll(Arrays.asList(toDo, inProgress, done));
		List<Column> saveTo2 = repositoryColumn.saveAll(Arrays.asList(toDo2, inProgress2, done2));
		List<Column> saveTo3 = repositoryColumn.saveAll(Arrays.asList(toDo3, inProgress3, done3));
		List<Column> saveTo4 = repositoryColumn.saveAll(Arrays.asList(toDo4, inProgress4, done4));
		List<Column> saveTo5 = repositoryColumn.saveAll(Arrays.asList(toDo5, inProgress5, done5));
		List<Column> saveTo6 = repositoryColumn.saveAll(Arrays.asList(toDo6, inProgress6, done6));
		
		List<User> list1 = new ArrayList<>();
		list1.add(user);
		list1.add(user2);
		
		List<User> list2 = new ArrayList<>();
		list1.add(user2);
		
		List<User> list3 = new ArrayList<>();
		list1.add(user3);
		
		Task task1 = new Task("Tarea1", "Descripcion1", 10, project1, list1, toDo);
		Task task2 = new Task("Tarea2", "Descripcion2", 8, project1, list1, inProgress);
		Task task3 = new Task("Tarea3", "Descripcion3", 7, project2, list2, toDo2);
		Task task4 = new Task("Tarea4", "Descripcion4", 18, project3, list3, inProgress3);
		Task task5 = new Task("Tarea5", "Descripcion5", 2, project2, list1, inProgress2);
		Task task6 = new Task("Tarea6", "Descripcion6", 17, project3, list3, done3);
	}

}
