package com.spring.Service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.Model.Profile;
import com.spring.Repository.ProfileRepository;

@Service
@Transactional
public class ProfileService extends AbstractService{

	@Autowired
	private ProfileRepository repository;
	
	public List<Profile> findAll() {
		return repository.findAll();
	}

	public Profile findOne(Integer id) {
		return repository.findById(id).orElse(null);
	}

	public Profile save(Profile p) {
		return repository.save(p);
	}

	public boolean delete(Integer id) {
		boolean checkIfExists = this.repository.existsById(id);
		if (checkIfExists) {
			repository.deleteById(id);
		}
		return checkIfExists;
	}
	
}
