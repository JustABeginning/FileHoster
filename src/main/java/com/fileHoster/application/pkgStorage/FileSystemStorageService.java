/* (C) 2025 */
package com.fileHoster.application.pkgStorage;

import com.fileHoster.application.pkgConfig.ConfigClass;
import com.fileHoster.application.pkgConfig.ConfigSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

@Service
public class FileSystemStorageService implements StorageTask {

	private static Path globRootLocation;

	@Autowired
	public FileSystemStorageService() {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);

		String storageDir = context.getBean(ConfigClass.CONFIG_BEAN, ConfigSet.class).getStorageDir();

		String keysDir = context.getBean(ConfigClass.CONFIG_BEAN, ConfigSet.class).getKeysDir();

		String storageLocation = storageDir + "/" + keysDir;

		context.close();

		globRootLocation = Paths.get(storageLocation);

		System.out.println("\n[+] AUTOWIRED FileSystemStorageService !\n");
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
			Files.createDirectories(globRootLocation);
		} catch (IOException e) {
			throw new StorageException("\n[-] Couldn't INITIALIZE Storage: " + globRootLocation + " !\n", e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(globRootLocation.toFile());
	}

	public static void write(String fileName, String content) {
		// TODO Auto-generated method stub
		Path destinationFile = globRootLocation.resolve(fileName).normalize().toAbsolutePath();
		if (!destinationFile.getParent().equals(globRootLocation.toAbsolutePath())) {
			// This is a security check
			throw new StorageException("\n[-] Can't STORE File OUTSIDE Directory: " + globRootLocation + " !\n");
		}
		try {
			Files.writeString(destinationFile, content);
		} catch (IOException e) {
			throw new StorageException("\n[-] Can't WRITE to File: " + destinationFile + " !\n", e);
		}
	}

}
