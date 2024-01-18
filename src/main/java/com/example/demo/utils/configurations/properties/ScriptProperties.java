package com.example.demo.utils.configurations.properties;

public class ScriptProperties {

	private String scriptType;
	private String scriptCommand;


	public ScriptProperties() {
		// default constructor, Jackson uses this constructor during deserialization.
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public String getScriptCommand() {
		return scriptCommand;
	}

	public void setScriptCommand(String scriptCommand) {
		this.scriptCommand = scriptCommand;
	}

}
