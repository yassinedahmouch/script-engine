package com.example.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.message.ResponseMessage;
import com.example.demo.model.Script;
import com.example.demo.repository.ScriptRepository;
import com.example.demo.utils.CommandExecutionUtils;

@Service
public class ScriptService {

	@Autowired
	ScriptRepository scriptRepository;
	
	private final CommandExecutionUtils commandExecutionUtils;

    @Autowired
    public ScriptService(CommandExecutionUtils commandExecutionUtils) {
        this.commandExecutionUtils = commandExecutionUtils;
    }

	public List<Script> findAll() {
		return scriptRepository.findAll();
	}

	public Script store(MultipartFile file, String path) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Script script = new Script(fileName, file.getContentType(), path, file.getBytes());

		return scriptRepository.save(script);
	}

	public Script getFile(int id) {
		return scriptRepository.findById(id).get();
	}

	public Stream<Script> getAllFiles() {
		return scriptRepository.findAll().stream();
	}

	public ResponseEntity<ResponseMessage> executeScript(MultipartFile file, String path) {

		String message = "";
		try {
			File tempFile = File.createTempFile("temp", file.getOriginalFilename());
			file.transferTo(tempFile);

			String scriptExtension = CommandExecutionUtils.getFileExtension(tempFile.getName());
			String command = commandExecutionUtils.getExecutionCommand(scriptExtension) + " "
					+ tempFile.getAbsolutePath();

			ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
			processBuilder.directory(new File(path))
						  .redirectError(ProcessBuilder.Redirect.INHERIT);

			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder output = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}

			String commandReturn = output.toString();
			int exitCode = process.waitFor();

			tempFile.delete();
			message = "Execution completed.\nExit code: " + exitCode + "\nOutput:\n" + commandReturn;
			return ResponseEntity.ok(new ResponseMessage(message));
		} catch (IOException e) {
			message = "Error executing file: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
		} catch (InterruptedException e) {
			message = "Error executing file: " + e.getMessage();
			Thread.currentThread().interrupt();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
		}

	}
}
