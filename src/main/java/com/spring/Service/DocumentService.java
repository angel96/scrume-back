package com.spring.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.DocumentDto;
import com.spring.Model.Document;
import com.spring.Model.DocumentType;
import com.spring.Model.Sprint;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Repository.DocumentRepository;
import com.spring.Security.UserAccountService;

@Service
@Transactional
public class DocumentService extends AbstractService {
	@Autowired
	private DocumentRepository documentRepo;
	@Autowired
	private SprintService sprintService;
	@Autowired
	private UserRolService userRolService;
	@Autowired
	private UserService userService;

	public Document findOne(int id) {
		return this.documentRepo.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested document not exists"));
	}
	
	public DocumentDto findOneDto(int id) {
		Document doc = this.findOne(id);
		checkUserOnTeam(UserAccountService.getPrincipal(), doc.getSprint().getProject().getTeam());
		return new DocumentDto(doc.getId(), doc.getName(), String.valueOf(doc.getType()), doc.getContent(), doc.getSprint().getId());
	}

	public List<DocumentDto> findAllBySprint(int sprintId) {
		Sprint sprint = this.sprintService.getOne(sprintId);
		checkUserOnTeam(UserAccountService.getPrincipal(), sprint.getProject().getTeam());
		List<Document> docs = this.documentRepo.findBySprint(sprint);
		return docs.stream().map(x -> new DocumentDto(x.getId(),x.getName(), String.valueOf(x.getType()), x.getContent(), sprint.getId()))
				.collect(Collectors.toList());
	}

	public DocumentDto save(DocumentDto document, int sprintId) {
		checkType(document.getType());
		Sprint sprint = this.sprintService.getOne(sprintId);
		checkUserOnTeam(UserAccountService.getPrincipal(), sprint.getProject().getTeam());
		Document entity = new Document(DocumentType.valueOf(document.getType()), document.getName(), document.getContent(), sprint);
		Document saved = this.documentRepo.saveAndFlush(entity);
		return new DocumentDto(saved.getId(), saved.getName(), saved.getType().toString(), saved.getContent(), saved.getSprint().getId());
	}

	public DocumentDto update(DocumentDto dto, int documentId) {
		checkType(dto.getType());
		Document db = this.findOne(documentId);
		checkUserOnTeam(UserAccountService.getPrincipal(), db.getSprint().getProject().getTeam());
		db.setContent(dto.getContent());
		db.setType(DocumentType.valueOf(dto.getType()));
		db.setName(dto.getName());
		db = this.documentRepo.saveAndFlush(db);
		return new DocumentDto(db.getId(), db.getName(),db.getType().toString(), db.getContent(), db.getSprint().getId());
	}

	public void delete(int idDoc) {
		checkEntityExists(idDoc);
		Document doc = this.findOne(idDoc);
		checkUserOnTeam(UserAccountService.getPrincipal(), doc.getSprint().getProject().getTeam());
		this.documentRepo.delete(doc);
	}

	private void checkUserOnTeam(UserAccount user, Team team) {
		User usuario = this.userService.getUserByPrincipal();
		if (!this.userRolService.isUserOnTeam(usuario, team))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"The user " + user.getUsername() + " does not belong to the team: " + team.getName());
	}

	private void checkType(String check) {
		if (check == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type cannot be null");
		} else {
			try {
				DocumentType.valueOf(check);
			} catch (IllegalArgumentException e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Type does not match any: " + Arrays.asList(DocumentType.values()).stream()
								.map(x -> String.valueOf(x)).collect(Collectors.joining(",")));
			}
		}
	}

	private void checkEntityExists(Integer check) {
		if (!this.documentRepo.existsById(check)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested document is not avaiable");
		}
	}

	public void flush() {
		this.documentRepo.flush();
	}

}