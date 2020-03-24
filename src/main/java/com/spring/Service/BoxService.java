package com.spring.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.Model.Box;
import com.spring.Model.Payment;
import com.spring.Model.User;
import com.spring.Repository.BoxRepository;

@Service
@Transactional
public class BoxService extends AbstractService {

	@Autowired
	private BoxRepository boxRepository;

	@Autowired
	private UserRolService serviceUserRole;

	@Autowired
	private TeamService serviceTeam;

	public List<Box> allBoxForRegistration() {
		return boxRepository.findAll();
	}

	public Box getOne(int id) {
		Box box = this.boxRepository.getOne(id);
		if (box != null) {
			return box;
		} else {
			return null;
		}

	}

	public Box getMinimumBoxOfATeam(int team) {
		return this.boxRepository.getBoxMoreRecently(team).stream().min(Comparator.comparingDouble(Box::getPrice))
				.orElse(null);
	}

	public void flush() {
		boxRepository.flush();
	}
}
