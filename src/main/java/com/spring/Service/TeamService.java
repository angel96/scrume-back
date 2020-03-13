package com.spring.Service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.spring.CustomObject.TeamDto;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserRol;
import com.spring.Repository.TeamRepository;

@Service
@Transactional
public class TeamService extends AbstractService {

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRolService userRolService;

	public TeamDto save(TeamDto teamDto) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		this.validateUserPrincipal(principal);
		Team teamEntity = modelMapper.map(teamDto, Team.class);
		Team teamDB = this.teamRepository.save(teamEntity);
		UserRol userRol = new UserRol();
		userRol.setAdmin(true);
		userRol.setTeam(teamDB);
		userRol.setUser(principal);
		this.userRolService.save(userRol);
		return modelMapper.map(teamDB, TeamDto.class);
	}

	public Team findOne(int teamId) {
		return this.teamRepository.getOne(teamId);
	}

	private void validateUserPrincipal(User principal) throws Exception{
		if(principal == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The user must be logged in");
		}
	}
}
