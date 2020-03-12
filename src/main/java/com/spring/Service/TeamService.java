package com.spring.Service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.CustomObject.TeamDto;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserRol;
import com.spring.Repository.TeamRepository;

@Service
@Transactional
public class TeamService extends AbstractService{

	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserRolService userRolService;
	
	public TeamDto save(TeamDto team) throws Exception {
		
		try {
			User principal = this.userService.getUserByPrincipal();
			Team teamEntity = new Team();
			teamEntity.setName(team.getName());
			Team teamDB = this.teamRepository.save(teamEntity);
			UserRol  userRol = new UserRol();
			userRol.setAdmin(true);
			userRol.setTeam(teamDB);
			userRol.setUser(principal);
			this.userRolService.save(userRol);
			return new ModelMapper().map(teamDB, TeamDto.class);
		}catch(Exception e) {
			throw new Exception("Error when saving the team");
		}
	}
	
	public Team findOne(int teamId) {
		return this.teamRepository.getOne(teamId);
	}
	
}
