package com.spring.JWT;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.spring.Model.UserAccount;
import com.spring.Repository.AbstractRepository;

@Repository
public interface JwtUserAccountRepository extends AbstractRepository<UserAccount> {
	Boolean existsByUsername(String username);

	Optional<UserAccount> findByUsername(String username);
}
