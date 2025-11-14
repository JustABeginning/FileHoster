/* (C) 2025 */
package com.fileHoster.application.pkgData;

import com.fileHoster.application.FileHosterApplication;
import com.fileHoster.application.pkgConfig.ConfigClass;
import com.fileHoster.application.pkgConfig.ConfigSet;
import com.fileHoster.application.pkgStorage.FileSystemStorageService;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ProcessData {

	private static ConfigSet globConfigSet;

	@Autowired
	public ProcessData() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
		globConfigSet = context.getBean(ConfigClass.CONFIG_BEAN, ConfigSet.class);
		context.close();
	}

	private static final List<String> apiKeyList = new ArrayList<>();

	private static String apiKey = null;

	private static String apiEndPoint;

	private static void getPermutn(String str, String ans, List<String> list) {

		// If string is empty
		if (str.isEmpty()) {
			list.add(ans);
			return;
		}

		for (int i = 0; i < str.length(); i++) {

			// ith character of str
			char ch = str.charAt(i);

			// Rest of the string after excluding
			// the ith character
			String ros = str.substring(0, i) + str.substring(i + 1);

			// Recursive call
			getPermutn(ros, ans + ch, list);
		}
	}

	public static void setAPIKey(String key, long wait) {
		getPermutn(key, "", apiKeyList);

		apiEndPoint = System.getProperty(FileHosterApplication.API_ENDPOINT, null);

		if (apiEndPoint == null) {
			apiEndPoint = globConfigSet.getApiEndPoint();
		}

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(() -> {
			// TODO Auto-generated method stub
			int i = 0, l = apiKeyList.size();
			while (true) {
				i = (int) Math.round((l - 1) * Math.random());
				apiKey = apiKeyList.get(i);
				//
				StringBuilder content = new StringBuilder(
						new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis())));
				content.append(" :: ").append(apiEndPoint);
				content.append(" : ").append(apiKey);
				//
				FileSystemStorageService.write(globConfigSet.getKeysFile(), content.toString());
				try {
					Thread.sleep(wait);
				} catch (InterruptedException ignored) {

				}
			}
		});
	}

	public static boolean compareAPIKey(String argsKey) {
		return argsKey.equals(apiKey);
	}

}
