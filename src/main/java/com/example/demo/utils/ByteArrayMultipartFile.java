
package com.example.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class ByteArrayMultipartFile implements MultipartFile {

	private final String name;
	private final String originalFilename;
	private final byte[] content;

	public ByteArrayMultipartFile(String name, String originalFilename, byte[] content) {
		this.name = name;
		this.originalFilename = originalFilename;
		this.content = content;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getOriginalFilename() {
		return originalFilename;
	}

	@Override
	public String getContentType() {
		// You can set the appropriate content type based on your requirements
		return "application/octet-stream";
	}

	@Override
	public boolean isEmpty() {
		return content.length == 0;
	}

	@Override
	public long getSize() {
		return content.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return content;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(content);
	}

	@Override
	public void transferTo(Path dest) throws IOException, IllegalStateException {
		Files.copy(getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		if (dest.isDirectory()) {
			throw new IllegalArgumentException("Destination file cannot be a directory.");
		}
		if (!dest.exists()) {
			dest.createNewFile();
		}

		try (InputStream inputStream = getInputStream(); FileOutputStream outputStream = new FileOutputStream(dest)) {
			int bytesRead;
			byte[] buffer = new byte[1024];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}

	}
}
