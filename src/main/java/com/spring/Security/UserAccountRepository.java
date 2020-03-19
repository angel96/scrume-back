package com.spring.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.model.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
	@Query("select ua from UserAccount ua where ua.username = ?1")
	Optional<UserAccount> findByUserName(String username);
}
