package com.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.CustomObject.UserRolDto;
import com.spring.Model.UserRol;

public interface UserRolRepository extends JpaRepository<UserRol, Long> {

}
