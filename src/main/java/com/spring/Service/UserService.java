package com.spring.Service;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.CustomObject.FindByNickDto;
import com.spring.CustomObject.UserForWorkspaceDto;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Repository.UserRepository;
import com.spring.Security.UserAccountService;

@Service
@Transactional
public class UserService extends AbstractService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRolService userRolService;

	@Autowired
	private TeamService teamService;
	
	public User getUserByPrincipal() {
		UserAccount userAccount = UserAccountService.getPrincipal();
		return this.userRepository.findByUserAccount(userAccount.getUsername()).orElse(null);
	}
	
	public User findOne(int userId) {
		return this.userRepository.findById(userId).orElse(null);
	}
	public void flush() {
		userRepository.flush();
	}
	
	public Collection<UserForWorkspaceDto> findByNickStartsWith(FindByNickDto findByNickDto){
		List<User> users = this.userRepository.findByNickStartsWith(findByNickDto.getString());
		Team team = this.teamService.findOne(findByNickDto.getTeam());
		Collection<Integer> idUsers = findByNickDto.getUsers();
		idUsers.addAll(this.userRolService.findIdUsersByTeam(team));
		users = users.stream().filter(u -> !idUsers.contains(u.getId())).collect(Collectors.toList());
		if(users.size() > 5) {
			users = users.subList(0,  4);
		}
		ModelMapper mapper = new ModelMapper();
		Type listType = new TypeToken<List<UserForWorkspaceDto>>() {
		}.getType();
		return mapper.map(users, listType);
	}
}
