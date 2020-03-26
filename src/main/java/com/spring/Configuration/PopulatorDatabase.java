package com.spring.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.spring.Model.Box;
import com.spring.Model.Column;
import com.spring.Model.Document;
import com.spring.Model.DocumentType;
import com.spring.Model.Estimation;
import com.spring.Model.HistoryTask;
import com.spring.Model.Invitation;
import com.spring.Model.Payment;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
import com.spring.Model.Task;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Model.UserRol;
import com.spring.Model.Workspace;
import com.spring.Repository.BoxRepository;
import com.spring.Repository.ColumnRepository;
import com.spring.Repository.DocumentRepository;
import com.spring.Repository.EstimationRepository;
import com.spring.Repository.HistoryTaskRepository;
import com.spring.Repository.InvitationRepository;
import com.spring.Repository.PaymentRepository;
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

	protected final Logger log = Logger.getLogger(PopulatorDatabase.class);

	@Autowired
	private UserAccountRepository accountRepository;

	@Autowired
	private BoxRepository boxRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private SprintRepository sprintRepository;

	@Autowired
	private WorkspaceRepository workspaceRepository;

	@Autowired
	private ColumnRepository columnRepository;

	@Autowired
	private UserRolRepository userRolRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private InvitationRepository invitationRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private HistoryTaskRepository historyTaskRepository;

	@Autowired
	private EstimationRepository estimationRepository;

	@Override
	public void run(String... args) throws Exception {

		final String properties = "entities.properties";

		SortedMap<String, Integer> entities = new TreeMap<>();
		Utiles.escribeFichero(entities, properties);

		estimationRepository.deleteAll();
		historyTaskRepository.deleteAll();
		paymentRepository.deleteAll();
		invitationRepository.deleteAll();
		taskRepository.deleteAll();
		userRolRepository.deleteAll();
		columnRepository.deleteAll();
		workspaceRepository.deleteAll();
		sprintRepository.deleteAll();
		projectRepository.deleteAll();
		teamRepository.deleteAll();
		userRepository.deleteAll();
		boxRepository.deleteAll();
		accountRepository.deleteAll();
		documentRepository.deleteAll();

		UserAccount account1 = accountRepository
				.save(new UserAccount("testuser1@gmail.com", Utiles.encryptedPassword("1234561"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		UserAccount account2 = accountRepository
				.save(new UserAccount("testuser2@gmail.com", Utiles.encryptedPassword("1234562"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		UserAccount account3 = accountRepository
				.save(new UserAccount("testuser3@gmail.com", Utiles.encryptedPassword("1234563"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		UserAccount account4 = accountRepository
				.save(new UserAccount("testuser4@gmail.com", Utiles.encryptedPassword("1234564"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));
		
		UserAccount account5 = accountRepository
				.save(new UserAccount("testuser5@gmail.com", Utiles.encryptedPassword("1234565"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN))));

		entities.put("account1", account1.getId());
		entities.put("account2", account2.getId());
		entities.put("account3", account3.getId());
		entities.put("account4", account4.getId());
		entities.put("account5", account5.getId());

		Box basicBox = boxRepository.save(new Box("BASIC", 0.0));
		Box standardBox = boxRepository.save(new Box("STANDARD", 1.0));
		Box proBox = boxRepository.save(new Box("PRO", 2.0));

		entities.put("basicBox", basicBox.getId());
		entities.put("standardBox", standardBox.getId());
		entities.put("proBox", proBox.getId());

		User user1 = new User("name1", "surnames1", "nick1", "gitUser1", null);
		user1.setUserAccount(account1);
		user1 = userRepository.save(user1);
		User user2 = new User("name2", "surnames2", "nick2", "gitUser2", null);
		user2.setUserAccount(account2);
		user2 = userRepository.save(user2);
		User user3 = new User("name3", "surnames3", "nick3", "gitUser3", null);
		user3.setUserAccount(account3);
		user3 = userRepository.save(user3);
		User user4 = new User("name4", "surnames4", "nick4", "gitUser4", null);
		user4.setUserAccount(account4);
		user4 = userRepository.save(user4);

		entities.put("user1", user1.getId());
		entities.put("user2", user2.getId());
		entities.put("user3", user3.getId());
		entities.put("user4", user4.getId());

		Team team1 = teamRepository.save(new Team("Team 1"));
		Team team2 = teamRepository.save(new Team("Team 2"));
		Team team3 = teamRepository.save(new Team("Team 3"));
		Team team4 = teamRepository.save(new Team("Team 4"));

		entities.put("team1", team1.getId());
		entities.put("team2", team2.getId());
		entities.put("team3", team3.getId());
		entities.put("team4", team4.getId());

		UserRol rol1 = this.userRolRepository.save(new UserRol(true, user1, team1));
		UserRol rol2 = this.userRolRepository.save(new UserRol(true, user2, team2));
		UserRol rol3 = this.userRolRepository.save(new UserRol(true, user3, team3));
		UserRol rol4 = this.userRolRepository.save(new UserRol(false, user4, team4));
		UserRol rol5 = this.userRolRepository.save(new UserRol(false, user4, team1));

		entities.put("rol1", rol1.getId());
		entities.put("rol2", rol2.getId());
		entities.put("rol3", rol3.getId());
		entities.put("rol4", rol4.getId());
		entities.put("rol5", rol5.getId());

		LocalDateTime localDateTime1 = LocalDateTime.of(2020, 2, 03, 10, 15);
		Date localDate1 = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime2 = LocalDateTime.of(2020, 2, 13, 10, 15);
		Date localDate2 = Date.from(localDateTime2.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime3 = LocalDateTime.of(2020, 12, 24, 10, 15);
		Date localDate3 = Date.from(localDateTime3.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime4 = LocalDateTime.of(2020, 1, 05, 10, 15);
		Date localDate4 = Date.from(localDateTime4.atZone(ZoneId.systemDefault()).toInstant());

		Invitation invitation1 = this.invitationRepository
				.save(new Invitation("Message 1", localDate1, true, user1, user4, team1));
		Invitation invitation2 = this.invitationRepository
				.save(new Invitation("Message 2", localDate2, false, user1, user3, team1));
		Invitation invitation3 = this.invitationRepository
				.save(new Invitation("Message 3", localDate3, null, user1, user2, team1));
		Invitation invitation4 = this.invitationRepository
				.save(new Invitation("Message 4", localDate4, null, user1, user3, team1));

		entities.put("invitation1", invitation1.getId());
		entities.put("invitation2", invitation2.getId());
		entities.put("invitation3", invitation3.getId());
		entities.put("invitation4", invitation4.getId());

		Project project1 = projectRepository.save(new Project("Proyect 1", "Proyect 1", team1));
		Project project2 = projectRepository.save(new Project("Proyect 2", "Proyect 2", team2));
		Project project3 = projectRepository.save(new Project("Proyect 3", "Proyect 3", team3));
		Project project4 = projectRepository.save(new Project("Proyect 4", "Proyect 4", team4));
		Project project5 = projectRepository.save(new Project("Proyect 5", "Proyect 5", team1));

		entities.put("project1", project1.getId());
		entities.put("project2", project2.getId());
		entities.put("project3", project3.getId());
		entities.put("project4", project4.getId());
		entities.put("project5", project5.getId());

		LocalDateTime localDateTime5 = LocalDateTime.of(2020, 3, 25, 10, 15);
		Date localDate5 = Date.from(localDateTime5.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime6 = LocalDateTime.of(2020, 4, 05, 10, 15);
		Date localDate6 = Date.from(localDateTime6.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime7 = LocalDateTime.of(2020, 10, 25, 10, 15);
		Date localDate7 = Date.from(localDateTime7.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime8 = LocalDateTime.of(2020, 11, 05, 10, 15);
		Date localDate8 = Date.from(localDateTime8.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime9 = LocalDateTime.of(2020, 7, 25, 10, 15);
		Date localDate9 = Date.from(localDateTime9.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime10 = LocalDateTime.of(2020, 8, 25, 10, 15);
		Date localDate10 = Date.from(localDateTime10.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime11 = LocalDateTime.of(2020, 8, 25, 10, 15);
		Date localDate11 = Date.from(localDateTime11.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime12 = LocalDateTime.of(2020, 9, 25, 10, 15);
		Date localDate12 = Date.from(localDateTime12.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime13 = LocalDateTime.of(2020, 8, 25, 10, 15);
		Date localDate13 = Date.from(localDateTime13.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime14 = LocalDateTime.of(2020, 12, 25, 10, 15);
		Date localDate14 = Date.from(localDateTime14.atZone(ZoneId.systemDefault()).toInstant());

		Sprint sprint1 = this.sprintRepository.save(new Sprint(localDate5, localDate6, project1));
		Sprint sprint2 = this.sprintRepository.save(new Sprint(localDate7, localDate8, project2));
		Sprint sprint3 = this.sprintRepository.save(new Sprint(localDate9, localDate10, project3));
		Sprint sprint4 = this.sprintRepository.save(new Sprint(localDate11, localDate12, project4));
		Sprint sprint5 = this.sprintRepository.save(new Sprint(localDate13, localDate14, project1));

		entities.put("sprint1", sprint1.getId());
		entities.put("sprint2", sprint2.getId());
		entities.put("sprint3", sprint3.getId());
		entities.put("sprint4", sprint4.getId());
		entities.put("sprint5", sprint5.getId());

		Workspace workspace1 = this.workspaceRepository.save(new Workspace("Workspace 1", sprint1));
		Workspace workspace2 = this.workspaceRepository.save(new Workspace("Workspace 2", sprint2));
		Workspace workspace3 = this.workspaceRepository.save(new Workspace("Workspace 3", sprint3));
		Workspace workspace4 = this.workspaceRepository.save(new Workspace("Workspace 4", sprint4));
		Workspace workspace5 = this.workspaceRepository.save(new Workspace("Workspace 5", sprint5));

		entities.put("workspace1", workspace1.getId());
		entities.put("workspace2", workspace2.getId());
		entities.put("workspace3", workspace3.getId());
		entities.put("workspace4", workspace4.getId());
		entities.put("workspace5", workspace5.getId());

		String toDoName = "To do";
		String inProgressName = "In progress";
		String doneName = "Done";

		Column toDo1 = this.columnRepository.save(new Column(toDoName, workspace1));
		Column inProgress1 = this.columnRepository.save(new Column(inProgressName, workspace1));
		Column done1 = this.columnRepository.save(new Column(doneName, workspace1));

		Column toDo2 = this.columnRepository.save(new Column(toDoName, workspace2));
		Column inProgress2 = this.columnRepository.save(new Column(inProgressName, workspace2));
		Column done2 = this.columnRepository.save(new Column(doneName, workspace2));

		Column toDo3 = this.columnRepository.save(new Column(toDoName, workspace3));
		Column inProgress3 = this.columnRepository.save(new Column(inProgressName, workspace3));
		Column done3 = this.columnRepository.save(new Column(doneName, workspace3));

		Column toDo4 = this.columnRepository.save(new Column(toDoName, workspace4));
		Column inProgress4 = this.columnRepository.save(new Column(inProgressName, workspace4));
		Column done4 = this.columnRepository.save(new Column(doneName, workspace4));

		Column toDo5 = this.columnRepository.save(new Column(toDoName, workspace5));
		Column inProgress5 = this.columnRepository.save(new Column(inProgressName, workspace5));
		Column done5 = this.columnRepository.save(new Column(doneName, workspace5));

		entities.put("toDo", toDo1.getId());
		entities.put("inProgress", inProgress1.getId());
		entities.put("done", done1.getId());

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

		Set<User> list1 = new HashSet<>();
		list1.add(user1);
		list1.add(user4);

		Set<User> list2 = new HashSet<>();
		list2.add(user1);

		Set<User> list3 = new HashSet<>();
		list3.add(user4);

		Task task1 = this.taskRepository.save(new Task("task1", "Description1", 10, project1, list1, toDo1));
		Task task2 = this.taskRepository.save(new Task("task2", "Description2", 8, project1, list1, inProgress1));
		Task task3 = this.taskRepository.save(new Task("task3", "Description3", 18, project1, list3, toDo5));
		Task task4 = this.taskRepository.save(new Task("task4", "Description4", 0, project1, list3, null));
		Task task5 = this.taskRepository.save(new Task("task5", "Description5", 0, project1, list2, null));
		Task task6 = this.taskRepository.save(new Task("task6", "Description6", 0, project1, list1, null));

		entities.put("task1", task1.getId());
		entities.put("task2", task2.getId());
		entities.put("task3", task3.getId());
		entities.put("task4", task4.getId());
		entities.put("task5", task5.getId());
		entities.put("task6", task6.getId());

		Document doc1 = this.documentRepository.save(new Document(DocumentType.DAILY, "Daily Test", "Prueba 1", sprint1));
		Document doc2 = this.documentRepository.save(new Document(DocumentType.PLANNING_MEETING,"Planning meeting Test", "Prueba 2", sprint1));
		Document doc3 = this.documentRepository.save(new Document(DocumentType.REVIEW, "Review Test", "Prueba 3", sprint1));
		Document doc4 = this.documentRepository.save(new Document(DocumentType.RETROSPECTIVE, "Retrospective Test", "Prueba 4", sprint1));

		entities.put("doc1", doc1.getId());
		entities.put("doc2", doc2.getId());
		entities.put("doc3", doc3.getId());
		entities.put("doc4", doc4.getId());

		HistoryTask historyTask1 = this.historyTaskRepository
				.save(new HistoryTask(localDateTime5, toDo1, inProgress1, task2));
		HistoryTask historyTask2 = this.historyTaskRepository
				.save(new HistoryTask(localDateTime5, toDo1, toDo5, task3));
		entities.put("historyTask1", historyTask1.getId());
		entities.put("historyTask2", historyTask2.getId());

		Estimation estimation1 = this.estimationRepository.save(new Estimation(5, user1, task1));
		Estimation estimation2 = this.estimationRepository.save(new Estimation(15, user4, task1));
		Estimation estimation3 = this.estimationRepository.save(new Estimation(4, user1, task2));
		Estimation estimation4 = this.estimationRepository.save(new Estimation(12, user4, task2));
		Estimation estimation5 = this.estimationRepository.save(new Estimation(18, user1, task3));
		Estimation estimation6 = this.estimationRepository.save(new Estimation(18, user4, task3));
		Estimation estimation7 = this.estimationRepository.save(new Estimation(10, user4, task4));
		Estimation estimation8 = this.estimationRepository.save(new Estimation(11, user4, task5));
		Estimation estimation9 = this.estimationRepository.save(new Estimation(14, user4, task6));

		entities.put("estimation1", estimation1.getId());
		entities.put("estimation2", estimation2.getId());
		entities.put("estimation3", estimation3.getId());
		entities.put("estimation4", estimation4.getId());
		entities.put("estimation5", estimation5.getId());
		entities.put("estimation6", estimation6.getId());
		entities.put("estimation7", estimation7.getId());
		entities.put("estimation8", estimation8.getId());
		entities.put("estimation9", estimation9.getId());

		Payment payment1 = this.paymentRepository.save(new Payment(LocalDate.from(localDateTime1), basicBox,
				user1.getUserAccount(), LocalDate.from(localDateTime2)));
		Payment payment2 = this.paymentRepository.save(new Payment(LocalDate.from(localDateTime2), proBox,
				user1.getUserAccount(), LocalDate.from(localDateTime3)));
		Payment payment3 = this.paymentRepository.save(new Payment(LocalDate.from(localDateTime1), basicBox,
				user4.getUserAccount(), LocalDate.from(localDateTime2)));
		Payment payment4 = this.paymentRepository.save(new Payment(LocalDate.from(localDateTime2), standardBox,
				user4.getUserAccount(), LocalDate.from(localDateTime3)));
		Payment payment5 = this.paymentRepository.save(new Payment(LocalDate.from(localDateTime2), proBox,
				user2.getUserAccount(), LocalDate.from(localDateTime3)));
		Payment payment6 = this.paymentRepository.save(new Payment(LocalDate.from(localDateTime2), proBox,
				user3.getUserAccount(), LocalDate.from(localDateTime3)));

		entities.put("payment1", payment1.getId());
		entities.put("payment2", payment2.getId());
		entities.put("payment3", payment3.getId());
		entities.put("payment4", payment4.getId());
		entities.put("payment5", payment5.getId());
		entities.put("payment6", payment6.getId());

		Utiles.escribeFichero(entities, properties);

		log.info("The entities mapped are: \n" + entities.keySet().stream().map(x -> {
			Integer value = entities.get(x);
			return x + "=" + value + "\n";
		}).collect(Collectors.joining()));

		userRolRepository.flush();
		columnRepository.flush();
		workspaceRepository.flush();
		taskRepository.flush();
		sprintRepository.flush();
		projectRepository.flush();
		teamRepository.flush();
		userRepository.flush();
		boxRepository.flush();
		accountRepository.flush();
		invitationRepository.flush();
		paymentRepository.flush();
		historyTaskRepository.flush();
		estimationRepository.flush();
		documentRepository.flush();
	}

}
