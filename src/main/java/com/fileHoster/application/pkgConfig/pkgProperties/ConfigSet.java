/* (C) 2025 */
package com.fileHoster.application.pkgConfig.pkgProperties;

public class ConfigSet {

	private String defaultValue;

	private String storageDir;

	private String keysDir;

	private String keysFile;

	private String apiCspNonce;

	private String apiEndPoint;

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getStorageDir() {
		return storageDir;
	}

	public void setStorageDir(String storageDir) {
		this.storageDir = storageDir;
	}

	public String getKeysDir() {
		return keysDir;
	}

	public void setKeysDir(String keysDir) {
		this.keysDir = keysDir;
	}

	public String getKeysFile() {
		return keysFile;
	}

	public void setKeysFile(String keysFile) {
		this.keysFile = keysFile;
	}

	public String getApiCspNonce() {
		return apiCspNonce;
	}

	public void setApiCspNonce(String apiCspNonce) {
		this.apiCspNonce = apiCspNonce;
	}

	public String getApiEndPoint() {
		return apiEndPoint;
	}

	public void setApiEndPoint(String apiEndPoint) {
		this.apiEndPoint = apiEndPoint;
	}

}
