package com.spring.Security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
	@Query("select ua from UserAccount ua where ua.username = ?1")
	Optional<UserAccount> findByEmail(String username);

	Boolean existsByUsername(String username);
}
