package com.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.Model.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {

}
