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
import java.util.SortedMap;
import java.util.TreeMap;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.spring.Model.Box;
import com.spring.Model.Column;
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
import com.spring.Repository.TaskRepository;
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

	private static final String file = "entities.properties";

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

	@Autowired
	private TaskRepository repositoryTask;

	@Override
	public void run(String... args) throws Exception {

		SortedMap<String, Integer> entities = new TreeMap<String, Integer>();
		Utiles.escribeFichero(entities, file);

		repositoryTask.deleteAll();
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

		entities.put("account", account.getId());

		UserAccount account2 = repositoryAccount
				.save(new UserAccount("angdellun2@gmail.com", Utiles.encryptedPassword("1234562"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		entities.put("account2", account2.getId());

		UserAccount userAccount = repositoryAccount
				.save(new UserAccount("testuser@gmail.com", Utiles.encryptedPassword("123456"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		entities.put("userAccount", userAccount.getId());

		UserAccount account3 = repositoryAccount
				.save(new UserAccount("testuser2@gmail.com", Utiles.encryptedPassword("123456"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		entities.put("account3", account3.getId());

		UserAccount account4 = repositoryAccount
				.save(new UserAccount("testuser3@gmail.com", Utiles.encryptedPassword("123456"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		entities.put("account4", account4.getId());

		UserAccount account5 = repositoryAccount
				.save(new UserAccount("testuser4@gmail.com", Utiles.encryptedPassword("123456"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		entities.put("account5", account5.getId());

		Box basicBox = new Box();
		basicBox.setName("BASIC");
		basicBox.setPrice(0.0);

		basicBox = boxRepository.save(basicBox);

		entities.put("basicBox", basicBox.getId());

		Box standardBox = new Box();
		standardBox.setName("STANDARD");
		standardBox.setPrice(1.0);
		standardBox = boxRepository.save(standardBox);

		entities.put("standardBox", standardBox.getId());

		Box proBox = new Box();
		proBox.setName("PRO");
		proBox.setPrice(2.0);
		proBox = boxRepository.save(proBox);
		entities.put("proBox", proBox.getId());

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
		user = userRepository.save(user);

		user2.setEndingBoxDate(date2);
		user2.setName("Name2");
		user2.setNick("nick2");
		user2.setSurnames("surnames2");
		user2.setUserAccount(account2);
		user2 = userRepository.save(user2);

		user3.setEndingBoxDate(date3);
		user3.setName("Name3");
		user3.setNick("nick3");
		user3.setSurnames("surnames3");
		user3.setUserAccount(account3);
		user3 = userRepository.save(user3);

		entities.put("user", user.getId());
		entities.put("user2", user2.getId());
		entities.put("user3", user3.getId());

		Team team1 = repositoryTeam.save(new Team("Equipo 1"));
		Team team2 = repositoryTeam.save(new Team("Equipo 2"));
		Team team3 = repositoryTeam.save(new Team("Equipo 3"));
		Team team4 = repositoryTeam.save(new Team("Equipo 4"));
		Team team5 = repositoryTeam.save(new Team("Equipo 5"));

		entities.put("team1", team1.getId());
		entities.put("team2", team2.getId());
		entities.put("team3", team3.getId());
		entities.put("team4", team3.getId());
		entities.put("team5", team3.getId());

		UserRol rol1 = this.repositoryUserRol.save(new UserRol(true, user, team1));
		UserRol rol2 = this.repositoryUserRol.save(new UserRol(true, user2, team2));
		UserRol rol3 = this.repositoryUserRol.save(new UserRol(true, user3, team3));

		entities.put("rol1", rol1.getId());
		entities.put("rol2", rol2.getId());
		entities.put("rol3", rol3.getId());

		Project project1 = repositoryProject.save(new Project("Proyecto 1", "Proyecto 1", team1));
		Project project2 = repositoryProject.save(new Project("Proyecto 2", "Proyecto 2", team1));
		Project project3 = repositoryProject.save(new Project("Proyecto 3", "Proyecto 3", team2));
		Project project4 = repositoryProject.save(new Project("Proyecto 4", "Proyecto 4", team2));
		Project project5 = repositoryProject.save(new Project("Proyecto 5", "Proyecto 5", team2));

		entities.put("project1", project1.getId());
		entities.put("project2", project2.getId());
		entities.put("project3", project3.getId());
		entities.put("project4", project4.getId());
		entities.put("project5", project5.getId());

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

		entities.put("sprint1", sprint1.getId());
		entities.put("sprint2", sprint2.getId());
		entities.put("sprint3", sprint3.getId());
		entities.put("sprint4", sprint4.getId());
		entities.put("sprint5", sprint5.getId());

		Workspace workspace1 = this.repositoryWorkspace.save(new Workspace("Este es el workspace de prueba", sprint1));
		Workspace workspace2 = this.repositoryWorkspace.save(new Workspace("Workspace 2", sprint2));
		Workspace workspace3 = this.repositoryWorkspace.save(new Workspace("Workspace 3", sprint3));
		Workspace workspace4 = this.repositoryWorkspace.save(new Workspace("Workspace 4", sprint4));
		Workspace workspace5 = this.repositoryWorkspace.save(new Workspace("Workspace 5", sprint5));
		Workspace workspace6 = this.repositoryWorkspace.save(new Workspace("Workspace 6", sprint6));

		entities.put("workspace1", workspace1.getId());
		entities.put("workspace2", workspace2.getId());
		entities.put("workspace3", workspace3.getId());
		entities.put("workspace4", workspace4.getId());
		entities.put("workspace5", workspace5.getId());
		entities.put("workspace6", workspace6.getId());

		Column toDo = this.repositoryColumn.save(new Column("To Do", workspace1));
		Column inProgress = this.repositoryColumn.save(new Column("In progress", workspace1));
		Column done = this.repositoryColumn.save(new Column("Done", workspace1));

		Column toDo2 = this.repositoryColumn.save(new Column("To Do", workspace2));
		Column inProgress2 = this.repositoryColumn.save(new Column("In progress", workspace2));
		Column done2 = this.repositoryColumn.save(new Column("Done", workspace2));

		Column toDo3 = this.repositoryColumn.save(new Column("To Do", workspace3));
		Column inProgress3 = this.repositoryColumn.save(new Column("In progress", workspace3));
		Column done3 = this.repositoryColumn.save(new Column("Done", workspace3));

		Column toDo4 = this.repositoryColumn.save(new Column("To Do", workspace4));
		Column inProgress4 = this.repositoryColumn.save(new Column("In progress", workspace4));
		Column done4 = this.repositoryColumn.save(new Column("Done", workspace4));

		Column toDo5 = this.repositoryColumn.save(new Column("To Do", workspace5));
		Column inProgress5 = this.repositoryColumn.save(new Column("In progress", workspace5));
		Column done5 = this.repositoryColumn.save(new Column("Done", workspace5));

		Column toDo6 = this.repositoryColumn.save(new Column("To Do", workspace6));
		Column inProgress6 = this.repositoryColumn.save(new Column("In progress", workspace6));
		Column done6 = this.repositoryColumn.save(new Column("Done", workspace6));

		entities.put("toDo", toDo.getId());
		entities.put("inProgress", inProgress.getId());
		entities.put("done", done.getId());

		entities.put("toDo2", toDo2.getId());
		entities.put("inProgress2", inProgress2.getId());
		entities.put("done2", done2.getId());

		entities.put("toDo3", toDo3.getId());
		entities.put("inProgress3", inProgress3.getId());
		entities.put("done3", done3.getId());

		entities.put("toDo4", toDo4.getId());
		entities.put("inProgress4", inProgress4.getId());
		entities.put("done4", done4.getId());

		entities.put("toDo5", toDo5.getId());
		entities.put("inProgress5", inProgress5.getId());
		entities.put("done5", done5.getId());

		entities.put("toDo6", toDo6.getId());
		entities.put("inProgress6", inProgress6.getId());
		entities.put("done6", done6.getId());

		List<User> list1 = new ArrayList<>();
		list1.add(user);
		list1.add(user2);

		List<User> list2 = new ArrayList<>();
		list1.add(user2);

		List<User> list3 = new ArrayList<>();
		list1.add(user3);

		Task task1 = this.repositoryTask.save(new Task("Tarea1", "Descripcion1", 10, project1, list1, toDo));
		Task task2 = this.repositoryTask.save(new Task("Tarea2", "Descripcion2", 8, project1, list1, inProgress));
		Task task3 = this.repositoryTask.save(new Task("Tarea3", "Descripcion3", 7, project2, list2, toDo2));
		Task task4 = this.repositoryTask.save(new Task("Tarea4", "Descripcion4", 18, project3, list3, inProgress3));
		Task task5 = this.repositoryTask.save(new Task("Tarea5", "Descripcion5", 2, project2, list1, inProgress2));
		Task task6 = this.repositoryTask.save(new Task("Tarea6", "Descripcion6", 17, project3, list3, done3));

		entities.put("task1", task1.getId());
		entities.put("task2", task2.getId());
		entities.put("task3", task3.getId());
		entities.put("task4", task4.getId());
		entities.put("task5", task5.getId());
		entities.put("task6", task6.getId());

		Utiles.escribeFichero(entities, file);

		System.out.println(entities);

		repositoryUserRol.flush();
		repositoryColumn.flush();
		repositoryWorkspace.flush();
		repositoryTask.flush();
		repositorySprint.flush();
		repositoryProject.flush();
		repositoryTeam.flush();
		userRepository.flush();
		boxRepository.flush();
		repositoryProfile.flush();
		repositoryAdmin.flush();
		repositoryAccount.flush();

	}

}
