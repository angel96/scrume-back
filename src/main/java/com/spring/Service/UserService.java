package com.spring.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.model.User;
import com.spring.model.UserAccount;
import com.spring.repository.UserRepository;
import com.spring.security.UserAccountService;

@Service
@Transactional
public class UserService extends AbstractService {

	@Autowired
	private UserRepository userRepository;

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
}
