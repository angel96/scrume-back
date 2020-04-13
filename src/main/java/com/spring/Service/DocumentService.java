package com.spring.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.spring.CustomObject.DocumentDto;
import com.spring.Model.Document;
import com.spring.Model.DocumentType;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Repository.DocumentRepository;
import com.spring.Security.UserAccountService;

@Service
@Transactional
public class DocumentService extends AbstractService {
	@Autowired
	private DocumentRepository documentRepo;
	@Autowired
	private SprintService sprintService;
	@Autowired
	private UserRolService userRolService;
	@Autowired
	private UserService userService;

	public Document findOne(int id) {
		return this.documentRepo.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested document not exists"));
	}

	public DocumentDto findOneDto(int id) {
		Document doc = this.findOne(id);
		checkUserOnTeam(UserAccountService.getPrincipal(), doc.getSprint().getProject().getTeam());
		return new DocumentDto(doc.getId(), doc.getName(), String.valueOf(doc.getType()), doc.getContent(),
				doc.getSprint().getId());
	}

	public List<DocumentDto> findAllBySprint(int sprintId) {
		Sprint sprint = this.sprintService.getOne(sprintId);
		checkUserOnTeam(UserAccountService.getPrincipal(), sprint.getProject().getTeam());
		List<Document> docs = this.documentRepo.findBySprint(sprint);
		return docs.stream().map(x -> new DocumentDto(x.getId(), x.getName(), String.valueOf(x.getType()),
				x.getContent(), sprint.getId())).collect(Collectors.toList());
	}

	public void saveDaily(String name, Sprint sprint) {
		this.documentRepo.saveAndFlush(new Document(DocumentType.DAILY, name, "[]", sprint, false));
	}

	public DocumentDto save(DocumentDto document, int sprintId) {
		checkType(document.getType());
		Sprint sprint = this.sprintService.getOne(sprintId);
		checkUserOnTeam(UserAccountService.getPrincipal(), sprint.getProject().getTeam());
		Document entity = new Document(DocumentType.valueOf(document.getType()), document.getName(),
				document.getContent(), sprint, false);
		Document saved = this.documentRepo.saveAndFlush(entity);
		return new DocumentDto(saved.getId(), saved.getName(), saved.getType().toString(), saved.getContent(),
				saved.getSprint().getId());
	}

	public DocumentDto update(DocumentDto dto, int documentId) {
		checkType(dto.getType());
		Document db = this.findOne(documentId);
		checkUserOnTeam(UserAccountService.getPrincipal(), db.getSprint().getProject().getTeam());
		db.setContent(dto.getContent());
		db.setType(DocumentType.valueOf(dto.getType()));
		db.setName(dto.getName());
		db = this.documentRepo.saveAndFlush(db);
		return new DocumentDto(db.getId(), db.getName(), db.getType().toString(), db.getContent(),
				db.getSprint().getId());
	}

	public Integer getDaily(int idSprint) {
		Integer res;
		Sprint sprint = this.sprintService.getOne(idSprint);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 2);
		Date actualDate = cal.getTime();
		String date = new SimpleDateFormat("dd/MM/yyyy").format(actualDate);
		String name = "Daily " + date;
		List<Integer> dailys = this.documentRepo.getDaily(sprint, name);
		if (!dailys.isEmpty()) {
			res = dailys.get(0);
		} else {
			res = -1;
		}
		return res;
	}

	public void delete(int idDoc) {
		checkEntityExists(idDoc);
		Document doc = this.findOne(idDoc);
		checkUserOnTeam(UserAccountService.getPrincipal(), doc.getSprint().getProject().getTeam());
		this.documentRepo.delete(doc);
	}

	private void checkUserOnTeam(UserAccount user, Team team) {
		User usuario = this.userService.getUserByPrincipal();
		if (!this.userRolService.isUserOnTeam(usuario, team))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"The user " + user.getUsername() + " does not belong to the team: " + team.getName());
	}

	private void checkType(String check) {
		if (check == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type cannot be null");
		} else {
			try {
				DocumentType.valueOf(check);
			} catch (IllegalArgumentException e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Type does not match any: " + Arrays.asList(DocumentType.values()).stream()
								.map(x -> String.valueOf(x)).collect(Collectors.joining(",")));
			}
		}
	}

	private void checkEntityExists(Integer check) {
		if (!this.documentRepo.existsById(check)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested document is not avaiable");
		}
	}

	public ByteArrayInputStream generatePdf(int doc) {
		DocumentDto dto = this.findOneDto(doc);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// 1. Configuración de cabeceras

		// Necesario para no confundir el objeto documento de las entidades con el
		// objeto documento de la libreria de PDF
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		try {

			String type = dto.getType();
			String title = dto.getName();
			String content = dto.getContent();

			Sprint sprint = sprintService.getOne(dto.getSprint());

			Date start = sprint.getStartDate();
			Date end = sprint.getEndDate();

			Project project = sprint.getProject();
			Team team = project.getTeam();

			PdfWriter.getInstance(document, out);
			document.open();

			document.setPageSize(PageSize.A4);

			BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);

			Font fontTitle = new Font(helvetica, 16, Font.BOLD);
			Font fontNormal = new Font(helvetica, 14, Font.NORMAL);

			Image img = Image.getInstance("files/logo.png");
			img.scalePercent(10);
			img.setAlignment(Element.ALIGN_RIGHT);

			// Cabecera

			PdfPTable table = new PdfPTable(3);

			table.setWidthPercentage(100);

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

			PdfPCell left = getCell(type, PdfPCell.ALIGN_LEFT, fontNormal);
			PdfPCell center = getCell(
					"Sprint " + format.format(start) + " - " + format.format(end) + "\n" + "Proyecto "
							+ project.getName() + "\n Equipo " + team.getName() + "\n Fecha de descarga "
							+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
					PdfPCell.ALIGN_CENTER, fontNormal);
			PdfPCell right = getCell("Text on the right", PdfPCell.ALIGN_RIGHT, fontNormal);

			right.addElement(img);

			table.addCell(left);
			table.addCell(center);
			table.addCell(right);

			document.add(table);

			// Contenido

			document.add(Chunk.NEWLINE);

			Paragraph p = new Paragraph(title, fontTitle);
			document.add(p);

			document.add(Chunk.NEWLINE);

			// Contenido para cada campo

			generateFieldsByType(document, DocumentType.valueOf(type), content, fontTitle, fontNormal);

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		document.close();
		return new ByteArrayInputStream(out.toByteArray());

	}

	private void generateFieldsByType(com.itextpdf.text.Document document, DocumentType type, String content,
			Font fontTitle, Font fontNormal) {

		JSONParser parser = new JSONParser();
		JSONObject object = null;
		JSONArray array = null;

		try {
			if (type.equals(DocumentType.DAILY)) {
				array = (JSONArray) parser.parse(content);
			} else {
				object = (JSONObject) parser.parse(content);
			}
		} catch (ParseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Parsing content action has not been possible");
		}

		Map<String, String> values = new HashMap<>();

		switch (type) {

		case DAILY:
			array.stream().forEach(x -> {
				JSONObject o = (JSONObject) x;
				String name = (String) o.get("name");
				String done = (String) o.get("done");
				String problems = (String) o.get("problems");
				values.put("Nombre", name);
				values.put("Realizado", done);
				values.put("Problemas", problems);
				crearBody(document, values, fontTitle, fontNormal);
			});
			break;
		case PLANNING_MEETING:
			String entrega = (String) object.get("entrega");
			String conseguir = (String) object.get("conseguir");
			values.put("Entregado", entrega);
			values.put("Conseguir", conseguir);
			crearBody(document, values, fontTitle, fontNormal);
			break;
		case MIDDLE_REVIEW:
		case REVIEW:
			String done = (String) object.get("done");
			String noDone = (String) object.get("noDone");
			String rePlanning = (String) object.get("rePlanning");
			values.put("Realizado", done);
			values.put("No realizado", noDone);
			values.put("Re-Planificación", rePlanning);
			crearBody(document, values, fontTitle, fontNormal);
			break;
		case MIDDLE_RETROSPECTIVE:
		case RETROSPECTIVE:
			String good = (String) object.get("good");
			String bad = (String) object.get("bad");
			String improvement = (String) object.get("improvement");
			values.put("Bien", good);
			values.put("Mal", bad);
			values.put("Mejora", improvement);
			crearBody(document, values, fontTitle, fontNormal);
			break;
		default:
			break;
		}

	}

	public void crearBody(com.itextpdf.text.Document document, Map<String, String> values, Font fontTitle,
			Font fontNormal) {

		values.keySet().forEach(x -> {
			try {
				String titulo = x;
				String contenido = values.get(x);

				PdfPTable contentTable1 = new PdfPTable(1);
				contentTable1.setWidthPercentage(100);
				PdfPCell fieldTable = getCell(titulo, PdfPCell.ALIGN_LEFT, fontTitle);
				contentTable1.addCell(fieldTable);
				document.add(contentTable1);

				document.add(Chunk.NEWLINE);

				PdfPTable contentTable2 = new PdfPTable(1);
				contentTable2.setWidthPercentage(100);
				PdfPCell fieldTable2 = getCell(contenido, PdfPCell.ALIGN_LEFT, fontNormal);
				contentTable2.addCell(fieldTable2);

				document.add(contentTable2);
				document.add(Chunk.NEWLINE);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		});

	}

	public void flush() {
		this.documentRepo.flush();
	}

	public Boolean checkDocumentIsCreated(String title, Sprint sprint) {

		title = title.toLowerCase();
		DocumentType type = null;
		if (title.contains("middle") && title.contains("review")) {
			type = DocumentType.MIDDLE_REVIEW;
		} else if (title.contains("middle") && title.contains("retrospective")) {
			type = DocumentType.MIDDLE_RETROSPECTIVE;
		} else if (title.contains("review")) {
			type = DocumentType.REVIEW;
		} else if (title.contains("retrospective")) {
			type = DocumentType.RETROSPECTIVE;
		}
		List<Document> documents = this.documentRepo.findBySprintAndTypeAndNotified(sprint, type, false);
		if (!documents.isEmpty()) {
			Document document = documents.get(0);
			document.setNotified(true);
			this.documentRepo.saveAndFlush(document);
		}
		return documents.size() > 0;
	}

	private PdfPCell getCell(String text, int aligment, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setPadding(0);
		cell.setHorizontalAlignment(aligment);
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;
	}

}