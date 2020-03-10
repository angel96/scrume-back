package com.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.Model.User;
import com.spring.Model.UserAccount;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUserAccount(UserAccount principal);
}
