package com.example.demo.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "SCRIPT")
public class Script {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "PATH")
	private String path;

	@Lob
	@Column(name = "FILE_SCRIPT")
	private byte[] fileScript;

	public Script() {
		super();
	}

	public Script(String name, String type, String path, byte[] fileScript) {
		this.name = name;
		this.type = type;
		this.path = path;
		this.fileScript = fileScript;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getFileScript() {
		return fileScript;
	}

	public void setFileScript(byte[] fileScript) {
		this.fileScript = fileScript;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "Script [id=" + id + ", name=" + name + ", type=" + type + ", fileScript=" + Arrays.toString(fileScript)
				+ "]";
	}

}
