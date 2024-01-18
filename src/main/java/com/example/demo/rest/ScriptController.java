package com.example.demo.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.message.ResponseFileScript;
import com.example.demo.message.ResponseMessage;
import com.example.demo.model.Script;
import com.example.demo.service.ScriptService;
import com.example.demo.utils.ByteArrayMultipartFile;

@RestController
@RequestMapping(value = "/welcome")
public class ScriptController {

	@Autowired
	private ScriptService scriptService;

	@GetMapping(value = "/scripts")
	public List<Script> findAll() {
		return scriptService.findAll();
	}

	@GetMapping("/files")
	public ResponseEntity<List<ResponseFileScript>> getListFiles() {
		List<ResponseFileScript> files = scriptService.getAllFiles().map((Script script) -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/welcome/files/")
					.path(Integer.toString(script.getId())).toUriString();

			return new ResponseFileScript(script.getName(), fileDownloadUri, script.getType(),
					script.getFileScript().length);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable int id) {
		Script script = scriptService.getFile(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + script.getName() + "\"")
				.body(script.getFileScript());
	}

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
												      @RequestParam("path") String path) {
		String message = "";
		try {

			scriptService.store(file, path);

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@PostMapping("/executeScript")
	public ResponseEntity<ResponseMessage> executeScript(@RequestParam("file") MultipartFile file,
														 @RequestParam("path") String path) {
		return scriptService.executeScript(file, path);
	}

	@PostMapping("/executeExistingScript/{id}")
	public ResponseEntity<ResponseMessage> executeExistingScript(@PathVariable int id) {
		Script script = scriptService.getFile(id);
		byte[] scriptContent = script.getFileScript();
		String scriptName = script.getName();
		String path = script.getPath();

		MultipartFile multipartFile = new ByteArrayMultipartFile(scriptName, scriptName, scriptContent);
		return scriptService.executeScript(multipartFile, path);
	}
}
