package com.spring.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.CustomObject.NoteDto;
import com.spring.Model.Note;
import com.spring.Service.NoteService;

@Controller
@RequestMapping("/api/note")
public class NoteApiController extends AbstractApiController {
	
	@Autowired
	private NoteService noteService;
	
	@GetMapping("showAll")
	public List<Note> showAll(){
		super.logger.info("GET /api/note/showAll");
		return this.noteService.findAllByUser();
	}
	
	@PostMapping
	public Note save(@RequestBody NoteDto dto) {
		super.logger.info("POST /api/note");
		return this.noteService.save(dto);
	}
	
	@PutMapping("{idNote}")
	public Note update(@PathVariable int idNote, @RequestBody NoteDto dto) {
		super.logger.info("PUT /api/note/" + idNote);
		return this.noteService.update(idNote, dto);
		
	}
	
	@DeleteMapping("{idNote}")
	public void delete(@PathVariable int idNote) {
		super.logger.info("DELETE /api/note/" + idNote);
		this.noteService.delete(idNote);
	}

}
