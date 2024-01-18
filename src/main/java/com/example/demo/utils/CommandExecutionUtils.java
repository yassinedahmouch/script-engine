package com.example.demo.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.utils.configurations.loader.Loader;
import com.example.demo.utils.configurations.properties.ScriptProperties;

@Component
public class CommandExecutionUtils {

	private final Loader dataLoadingService;

	@Autowired
	public CommandExecutionUtils(Loader dataLoadingService) {
		this.dataLoadingService = dataLoadingService;
	}

	public String getExecutionCommand(String scriptExtension) {
		List<ScriptProperties> scriptProperties = dataLoadingService.getScriptProperties();
		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			for (ScriptProperties scriptPropertie : scriptProperties) {
				if (scriptExtension.equalsIgnoreCase(scriptPropertie.getScriptType())) {
					return "cmd.exe /c start " + scriptPropertie.getScriptCommand();
				}
			}
		} 
		// Currently the only environment supported is Windows.
		return "";
	}

	public static String getFileExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf(".");
		if (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) {
			return fileName.substring(lastDotIndex + 1);
		}
		return "";
	}
}
