package com.spring.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.Model.Profile;
import com.spring.Service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileApiController extends AbstractApiController {

	@Autowired
	private ProfileService service;

	@GetMapping("/list")
	@ResponseBody
	public ResponseEntity<?> findAll() {

		ResponseEntity<?> result = null;

		try {
			result = new ResponseEntity<>(service.findAll(), HttpStatus.OK);
			super.logger.info("/api/list" + HttpStatus.OK);
		} catch (final Throwable oops) {
			super.logger.error(oops);
			result = new ResponseEntity<>("No esta autorizado", HttpStatus.UNAUTHORIZED);
			super.logger.error("GET /api/list" + HttpStatus.UNAUTHORIZED);
		}

		return result;
	}

	@GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findProfile(@PathVariable final int id) {
		ResponseEntity<?> result = null;

		try {
			result = new ResponseEntity<>(service.findOne(id), HttpStatus.OK);
		} catch (final Throwable oops) {
			result = new ResponseEntity<>("Element not found", HttpStatus.NOT_FOUND);
			super.logger.error("GET /api/profile/" + id + " =>" + HttpStatus.NOT_FOUND);

		}

		return result;
	}

	@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> save(@RequestBody final Profile profile) {

		ResponseEntity<?> result = null;

		try {
			System.out.println(profile);
			result = new ResponseEntity<>(this.service.save(profile), HttpStatus.OK);
		} catch (final Throwable oops) {
			result = new ResponseEntity<>("Element not found", HttpStatus.BAD_REQUEST);
			super.logger.error("POST /api/profile/ => ( " + profile + " ) => " + HttpStatus.NOT_FOUND);
		}

		return result;
	}

	@GetMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable final int id) {

		ResponseEntity<?> result = null;

		try {
			if (this.service.delete(id)) {
				result = new ResponseEntity<>("Profile deleted", HttpStatus.OK);
			} else {
				result = new ResponseEntity<>("Element does not exist", HttpStatus.BAD_REQUEST);
				super.logger.error("DELETE /api/profile/ => ( " + id + " ) => " + HttpStatus.BAD_REQUEST);
			}

		} catch (final Throwable oops) {
			result = new ResponseEntity<>("Element not found", HttpStatus.NOT_FOUND);
			super.logger.error("DELETE /api/profile/ => ( " + id + " ) => " + HttpStatus.NOT_FOUND);
		}

		return result;
	}

}
