/* (C) 2025 */
package com.fileHoster.application.pkgDB.pkgEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FileEntry {

	@Id
	private String entryID;

	private byte[] entryFileBytes;

	public String getEntryID() {
		return entryID;
	}

	public void setEntryID(String entryID) {
		this.entryID = entryID;
	}

	public byte[] getEntryFileBytes() {
		return entryFileBytes;
	}

	public void setEntryFileBytes(byte[] entryFileBytes) {
		this.entryFileBytes = entryFileBytes;
	}

}
