package com.spring.Service;

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

	public Box getMinimumBoxOfATeam(Team team) {
		Box res;
		List<Box> boxes = this.boxRepository.getMinimumBoxOfATeam(team);
		if(!boxes.isEmpty()) {
			res = boxes.get(0);
		}else {
			res = null;
		}
		return res;
	}
	
	public Box findOne(Integer idBox) {
		return boxRepository.findById(idBox).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested box doesn´t exists"));
	}
	
	public void flush() {
		boxRepository.flush();
	}
}
