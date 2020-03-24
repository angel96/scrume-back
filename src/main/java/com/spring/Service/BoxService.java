package com.spring.Service;

import java.util.Comparator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.Model.Box;
import com.spring.Model.Team;
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
		Box box = this.boxRepository.getOne(id);
		if (box != null) {
			return box;
		} else {
			return null;
		}

	}

	public Box getMinimumBoxOfATeam(int team) {

		Box res = null;

		try {
			List<Box> boxes = this.boxRepository.getMinimumBoxOfATeam(team);
			boxes.stream().forEach(x -> System.out.println(x.getName() + "" + x.getPrice()));
			if (!boxes.isEmpty()) {
				res = boxes.stream().min(Comparator.comparingDouble(Box::getPrice)).orElse(null);
			} else {
				res = null;
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"A problem has happened when fetching the minimum members team box.");
		}

		return res;
	}

	public void flush() {
		boxRepository.flush();
	}
}
