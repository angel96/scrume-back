package com.spring.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.DocumentDto;
import com.spring.Service.DocumentService;

@RestController
@RequestMapping("/api/document/")
public class DocumentApiController extends AbstractApiController {
	@Autowired
	private DocumentService documentService;

	@GetMapping("doc/{idDocument}")
	public DocumentDto getDocument(@PathVariable int idDocument) {
		super.logger.info("GET /api/document/doc/" + idDocument);
		return this.documentService.findOneDto(idDocument);

	}

	@GetMapping("sprint/{idSprint}")
	public List<DocumentDto> showAllBySprint(@PathVariable int idSprint) {
		super.logger.info("GET /api/document/sprint/" + idSprint);
		return this.documentService.findAllBySprint(idSprint);
	}

	@PostMapping("{idSprint}")
	public DocumentDto save(@PathVariable int idSprint, @RequestBody DocumentDto dto) {
		super.logger.info("POST /api/document/" + idSprint);
		return this.documentService.save(dto, idSprint);
	}

	@PutMapping("{idDocument}")
	public DocumentDto update(@PathVariable int idDocument, @RequestBody DocumentDto dto) {
		super.logger.info("PUT /api/document/" + idDocument);
		return this.documentService.update(dto, idDocument);

	}

	@DeleteMapping("{idDocument}")
	public void delete(@PathVariable int idDocument) {
		super.logger.info("DELETE /api/document/" + idDocument);
		this.documentService.delete(idDocument);
	}

	@RequestMapping(value = "doc-pdf/{idDocument}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> getPdfDocument(@PathVariable int idDocument) {
		super.logger.info("GET /api/document/doc-pdf/" + idDocument);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=documents-pdf-" + idDocument + ".pdf");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(this.documentService.generatePdf(idDocument)));
	}
}
