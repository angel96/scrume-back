package com.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.model.User;

@Repository
public interface UserRepository extends AbstractRepository<User> {

	@Query("select u from User u where u.userAccount.username = ?1")
	Optional<User> findByUserAccount(String username);
}
