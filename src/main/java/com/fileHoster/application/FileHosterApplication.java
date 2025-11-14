/* (C) 2025 */
package com.fileHoster.application;

import com.fileHoster.application.pkgData.ProcessData;
import com.fileHoster.application.pkgStorage.StorageTask;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FileHosterApplication {

	public static final String API_ENDPOINT = "api.endPoint";

	static {
		System.setProperty("application.configFile", "classpath:/properties/config.properties");
	}

	public static void main(String[] args) {
		if (args.length > 1) {

			String apiKey;
			int wait = 0;

			if (args.length > 2) {
				System.setProperty(API_ENDPOINT, args[0]);

				apiKey = args[1];
				try {
					wait = Integer.parseInt(args[2]);

				} catch (NumberFormatException e) {
					System.out.println("\n[-] Time (in min): " + args[2] + " MUST be a NUMBER !\n");
				}
			} else {
				apiKey = args[0];
				try {
					wait = Integer.parseInt(args[1]);

				} catch (NumberFormatException e) {
					System.out.println("\n[-] Time (in min): " + args[1] + " MUST be a NUMBER !\n");
				}
			}
			//
			SpringApplication.run(FileHosterApplication.class, args);
			//
			if (wait > 0)
				ProcessData.setAPIKey(apiKey, wait * 60000L);
			else
				System.out.println("\n[-] Invalid Time (in min) = " + wait + " !\n");

		} else {
			System.out.println("\n[-] Not Enough ARGUMENTS !\n");
		}
	}

	@Bean
	CommandLineRunner init(StorageTask storageTask) {
		return (args) -> {
			storageTask.deleteAll();
			storageTask.init();
		};
	}

}
