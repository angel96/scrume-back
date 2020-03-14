package com.spring.Service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Repository.UserRepository;
import com.spring.Security.UserAccountService;

@Service
@Transactional
public class UserService extends AbstractService {

	@Autowired
	private UserRepository userRepository;

	public User getUserByPrincipal() {
		UserAccount userAccount = UserAccountService.getPrincipal();
		return this.userRepository.findByUserAccount(userAccount.getUsername()).orElse(null);
	}
	
	public User findOne(int userId) throws Exception {
		return this.userRepository.findById(userId).orElse(null);
	}
	
}
