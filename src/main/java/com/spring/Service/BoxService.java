package com.spring.Service;

import java.util.Comparator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.Model.Box;
import com.spring.Repository.BoxRepository;

@Service
@Transactional
public class BoxService extends AbstractService {

	@Autowired
	private BoxRepository boxRepository;

	public List<Box> allBoxForRegistration() {
		return boxRepository.findAll();
	}

	public Box getOne(int id) {
		return this.boxRepository.getOne(id);
	}

	public Box getMinimumBoxOfATeam(int team) {
		return this.boxRepository.getBoxMoreRecently(team).stream().min(Comparator.comparingDouble(Box::getPrice))
				.orElse(null);
	}

	
	public Box findOne(Integer idBox) {
		return boxRepository.findById(idBox).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested box doesn´t exists"));
	}
	
	public void flush() {
		boxRepository.flush();
	}
}
