/* (C) 2025 */
package com.fileHoster.application.pkgConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("${application.configFile}")
public class ConfigClass {

	public static final String CONFIG_BEAN = "getConfigSet";

	@Value("${config.defaultValue}")
	private String defaultValue;

	@Value("${config.storageDir}")
	private String storageDir;

	@Value("${storage.keysDir}")
	private String keysDir;

	@Value("${storage.keysFile}")
	private String keysFile;

	@Value("${config.api}")
	private String apiEndPoint;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ConfigSet getConfigSet() {
		ConfigSet configSet = new ConfigSet();
		configSet.setDefaultValue(defaultValue);
		configSet.setStorageDir(storageDir);
		configSet.setKeysDir(keysDir);
		configSet.setKeysFile(keysFile);
		configSet.setApiEndPoint(apiEndPoint);
		return configSet;
	}

}
