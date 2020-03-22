package com.spring.Service;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.HistoryTaskDto;
import com.spring.Model.Column;
import com.spring.Model.HistoryTask;
import com.spring.Model.Task;
import com.spring.Model.Workspace;
import com.spring.Repository.HistoryTaskRepository;

@Service
@Transactional
public class HistoryTaskService extends AbstractService {

	@Autowired
	private HistoryTaskRepository repository;

	@Autowired
	private ColumnService serviceColumn;

	@Autowired
	private TaskService serviceTask;

	@Autowired
	private WorkspaceService serviceWorkspace;

	public Collection<HistoryTask> findHistoricalByWorkspace(int workspace) {

		Workspace w = serviceWorkspace.findOne(workspace);

		serviceWorkspace.checkMembers(w.getSprint().getProject().getTeam().getId());

		Collection<HistoryTask> result = this.repository.findHistoricalByWorkspace(workspace);

		return result;
	}

	public HistoryTaskDto save(HistoryTaskDto dto) {

		Column destiny = this.serviceColumn.findOne(dto.getDestiny());
		Task task = serviceTask.findOne(dto.getTask());
		Column origin = task.getColumn();

		Collection<Column> columns = this.repository.findColumnsByTeamId(task.getProject().getTeam().getId());

		HistoryTaskDto dtoToReturn = null;

		if (origin == null) {
			if (!columns.contains(destiny)) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This access is ilegal");
			} else {
				task.setColumn(destiny);
			}
		} else {
			if (!(columns.contains(origin) && columns.contains(destiny))) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This access is ilegal");
			} else {
				HistoryTask historyTask = new HistoryTask(LocalDateTime.now(), origin, destiny, task);

				HistoryTask saveTo = this.repository.saveAndFlush(historyTask);

				dtoToReturn = new HistoryTaskDto(saveTo.getDestiny().getId(), saveTo.getTask().getId());

				task.setColumn(destiny);
			}
		}

		return dtoToReturn;
	}

	public void flush() {
		repository.flush();
	}
}
