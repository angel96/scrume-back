package com.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.Model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

}
