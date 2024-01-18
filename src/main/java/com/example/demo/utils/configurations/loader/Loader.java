package com.example.demo.utils.configurations.loader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.demo.utils.configurations.properties.ScriptProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Loader {
	
	private List<ScriptProperties> scriptProperties;

	@PostConstruct
	public void readJsonFile() throws IOException {

		try {
			ObjectMapper objectMapper = new ObjectMapper();

			ClassPathResource resource = new ClassPathResource("configurations/script/script.json");
			File file = resource.getFile();

			this.scriptProperties = objectMapper.readValue(file, new TypeReference<List<ScriptProperties>>() {});
		} catch (IOException e) {
			throw new IOException("An error occurred while reading the JSON file.", e);
		}
	}
	
	public List<ScriptProperties> getScriptProperties() {
        return scriptProperties;
    }
}
