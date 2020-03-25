package com.spring.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.DocumentDto;
import com.spring.Service.DocumentService;

@RestController
@RequestMapping("api/document")
public class DocumentApiController extends AbstractApiController {
	@Autowired
	private DocumentService documentService;
	
	@GetMapping("{idSprint}")
	public List<DocumentDto> showAllBySprint(@PathVariable int idSprint){
		super.logger.info("GET /api/document/" + idSprint);
		return this.documentService.findAllBySprint(idSprint);
	}
	
	@PostMapping("{idSprint}")
	public DocumentDto save(@PathVariable int idSprint, @RequestBody DocumentDto dto) {
		return this.documentService.save(dto, idSprint);
	}
	
	@PutMapping("{idDocument}")
	public DocumentDto update(@PathVariable int idDocument, @RequestBody DocumentDto dto) {
		return this.documentService.update(dto, idDocument);
		
	}
	
	@DeleteMapping("{idDocument}")
	public void delete(@PathVariable int idDocument) {
		this.documentService.delete(idDocument);
	}

}
