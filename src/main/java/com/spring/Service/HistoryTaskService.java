package com.spring.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.HistoryTaskDto;
import com.spring.Model.Column;
import com.spring.Model.HistoryTask;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
import com.spring.Model.Task;
import com.spring.Model.Workspace;
import com.spring.Repository.HistoryTaskRepository;

@Service
@Transactional
public class HistoryTaskService extends AbstractService {

	protected final Logger log = Logger.getLogger(HistoryTaskService.class);

	@Autowired
	private HistoryTaskRepository repository;

	@Autowired
	private ColumnService serviceColumn;

	@Autowired
	private TaskService serviceTask;

	@Autowired
	private BoxService boxService;

	@Autowired
	private WorkspaceService serviceWorkspace;

	public Collection<HistoryTask> findHistoricalByWorkspace(int workspace) {

		Collection<HistoryTask> result = null;

		if (this.serviceWorkspace.checksIfExists(workspace)) {
			Workspace w = serviceWorkspace.findOne(workspace);
			serviceWorkspace.checkMembers(w.getSprint().getProject().getTeam().getId());
			result = this.repository.findHistoricalByWorkspace(workspace);
		} else {
			result = new ArrayList<>();
		}

		return result;
	}

	public HistoryTaskDto save(HistoryTaskDto dto) {

		Column destiny = this.serviceColumn.findOne(dto.getDestiny());
		this.validateBoxPrivileges(destiny.getWorkspace().getSprint(), destiny.getWorkspace());
		Task task = serviceTask.findOne(dto.getTask());
		Column origin = task.getColumn();
		if(origin != null && origin.getName().equals("Done")) {
			HistoryTask historyTask = this.repository.findByTaskAndDestiny(task, origin);
			this.repository.delete(historyTask);
		}
		Project projectColumnDestiny = destiny.getWorkspace().getSprint().getProject();
		Project taskProject = task.getProject();
		this.serviceWorkspace.checkMembers(taskProject.getTeam().getId());

		if (!projectColumnDestiny.getId().equals(taskProject.getId())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tasks from different projects can not be moved");
		}

		Collection<Column> columns = this.repository.findColumnsByTeamId(task.getProject().getTeam().getId());
		HistoryTaskDto dtoToReturn = null;
		if (origin == null) {
			if (!columns.contains(destiny)) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,
						"The task assigned to this workspace can not be moved. You are not member of this team");
			} else {
				task.setColumn(destiny);
			}
		} else {
			if (!columns.contains(destiny)) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,
						"The task assigned to this workspace can not be moved. You are not member of this team");
			} else {
				HistoryTask historyTask = new HistoryTask(LocalDateTime.now(), origin, destiny, task);

				HistoryTask saveTo = this.repository.saveAndFlush(historyTask);

				dtoToReturn = new HistoryTaskDto(saveTo.getDestiny().getId(), saveTo.getTask().getId());

				task.setColumn(destiny);
			}
		}

		return dtoToReturn;
	}

	private void validateBoxPrivileges(Sprint sprint, Workspace workspace) {
		if (this.boxService.getMinimumBoxOfATeam(sprint.getProject().getTeam().getId()).getName() == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"There is no payment record in the database, so you cannot manage workspaces");
		}
		LocalDateTime validDate = sprint.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		validDate = validDate.plusDays(30);
		if (this.boxService.getMinimumBoxOfATeam(sprint.getProject().getTeam().getId()).getName().equals("BASIC")
				&& (!this.serviceWorkspace.getFirstWorkspacesOfASprint(sprint, 1).contains(workspace)
						|| validDate.isBefore(LocalDateTime.now(ZoneId.systemDefault())))) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The minimum team box is basic, so you can only manage the first of your workspaces during the 30 days of the sprint");
		}
		if (this.boxService.getMinimumBoxOfATeam(sprint.getProject().getTeam().getId()).getName().equals("STANDARD")
				&& !this.serviceWorkspace.getFirstWorkspacesOfASprint(sprint, 2).contains(workspace)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The minimum team box is standard, so you are only allowed to manage your first two workspaces");
		}
	}

	public void flush() {
		repository.flush();
	}

	private Integer getPointsDoneTasks(Sprint sprint, int i) {
		LocalDate start = sprint.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		start = start.plusDays(i);
		LocalTime startTime = LocalTime.of(0, 0, 0);
		LocalTime endTime = LocalTime.of(23, 59, 59);
		LocalDateTime startDateTime = LocalDateTime.of(start, startTime);
		LocalDateTime endDateTime = LocalDateTime.of(start, endTime);
		List<Integer> pointsDone = this.repository.findBySprintAndDay(sprint, startDateTime, endDateTime);
		Integer pointsDoneTasks = 0;
		if (!pointsDone.isEmpty()) {
			pointsDoneTasks = pointsDone.stream().mapToInt(x -> x).sum();
		}
		return pointsDoneTasks;
	}
	
	public Long getPointsBurndown(Sprint sprint, int i, Long totalPoints) {
		Integer pointsDoneTasks = this.getPointsDoneTasks(sprint, i);
		return totalPoints - pointsDoneTasks;
	}
	
	public Long getPointsBurnup(Sprint sprint, int i, Long totalPoints) {
		Integer pointsDoneTasks = this.getPointsDoneTasks(sprint, i);
		return totalPoints + pointsDoneTasks;
	}

}
