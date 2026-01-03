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

	public static final String API_CORS_ORIGIN = "api.corsOrigin";

	static {
		System.setProperty("application.configFile", "classpath:/properties/config.properties");
	}

	public static void main(String[] args) {
		if (args.length > 1) {

			String apiKey;
			int index = 0, wait = 0;

			if (args.length > 3) {
				StringBuilder builder = new StringBuilder();
				while (index < args.length - 3)
					builder.append(args[index++]).append(" ");
				System.setProperty(API_CORS_ORIGIN, builder.toString().trim());
			}
			if (args.length > 2) {
				System.setProperty(API_ENDPOINT, args[index++]);
			}

			apiKey = args[index++];
			try {
				wait = Integer.parseInt(args[index]);

			} catch (NumberFormatException e) {
				System.out.println("\n[-] Time (in min): " + args[index] + " MUST be a NUMBER !\n");
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
