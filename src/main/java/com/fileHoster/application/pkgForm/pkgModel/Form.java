/* (C) 2025 */
package com.fileHoster.application.pkgForm.pkgModel;

import com.fileHoster.application.pkgForm.pkgValidator.annotation.FileValidation;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class Form {

	@NotEmpty(message = "{APIKeyValidation.Message}") private String apiKey;

	@NotEmpty(message = "{FileNameValidation.Message}") private String fileName;

	@FileValidation
	private MultipartFile file;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
