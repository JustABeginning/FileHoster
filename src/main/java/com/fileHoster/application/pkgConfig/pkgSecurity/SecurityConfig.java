/* (C) 2025 */
package com.fileHoster.application.pkgConfig.pkgSecurity;

import com.fileHoster.application.FileHosterApplication;
import com.fileHoster.application.pkgConfig.pkgProperties.ConfigClass;
import com.fileHoster.application.pkgConfig.pkgSecurity.pkgFilter.CSPNonceFilter;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.lang.Nullable;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("${application.configFile}")
@EnableMethodSecurity
public class SecurityConfig {

	@Value("${config.api}")
	private String apiEndPoint;

	/* -- */
	@Value("${api.getAllFiles}")
	private String apiGetAllFiles;

	@Value("${api.renameFile}")
	private String apiRenameFile;

	@Value("${api.deleteFile}")
	private String apiDeleteFile;

	@Value("${api.deleteAllFiles}")
	private String apiDeleteAllFiles;
	/* -- */
	/* -- */
	private static final int CSRF_IGNORED_MAPPINGS_NUM = 4;
	private static final String MAPPINGS_JOIN = "/";
	private static final int CORS_MAPPINGS_NUM = 1;
	private static final String[] ALLOWED_CORS_METHODS = {"GET", "POST"};
	/* -- */

	private static String allowedCorsOrigin;

	@Value("${api.corsOrigin}")
	private void setAllowedCorsOrigin(String allowedCorsOrigin) {
		SecurityConfig.allowedCorsOrigin = allowedCorsOrigin;
	}

	@Value("${api.csPolicy}")
	private String csPolicy;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurerForSecurity() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public String[] getCORSMappings() {
		String[] mappings = new String[CORS_MAPPINGS_NUM];
		mappings[0] = ConfigClass.API_HOME;
		return mappings;
	}

	@Bean
	public String[] getCSRFIgnoredMappings() {
		String apiEndPoint = System.getProperty(FileHosterApplication.API_ENDPOINT, null);
		if (apiEndPoint == null)
			apiEndPoint = this.apiEndPoint;
		apiEndPoint = MAPPINGS_JOIN + apiEndPoint + MAPPINGS_JOIN;
		String[] mappings = new String[CSRF_IGNORED_MAPPINGS_NUM];
		mappings[0] = apiEndPoint + apiGetAllFiles;
		mappings[1] = apiEndPoint + apiRenameFile;
		mappings[2] = apiEndPoint + apiDeleteFile;
		mappings[3] = apiEndPoint + apiDeleteAllFiles;
		return mappings;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@Nullable CorsRegistry registry) {
				if (registry != null) {
					String[] mappings = getCORSMappings();
					String[] allowedCorsOrigins;
					String tmpAllowedCorsOrigins = System.getProperty(FileHosterApplication.API_CORS_ORIGIN, null);
					if (tmpAllowedCorsOrigins == null)
						allowedCorsOrigins = new String[]{SecurityConfig.allowedCorsOrigin};
					else
						allowedCorsOrigins = tmpAllowedCorsOrigins.split(" ");
					for (String mapping : mappings)
						registry.addMapping(mapping).allowedOrigins(allowedCorsOrigins)
								.allowedMethods(ALLOWED_CORS_METHODS);
					System.out.println("\n[+] Configured CORS For:\n\n" + Arrays.toString(mappings)
							+ "\n\n[*] With ORIGIN(s):\n\n" + Arrays.toString(allowedCorsOrigins) + "\n");
				}
			}
		};
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		String[] mappings = getCSRFIgnoredMappings();
		System.out.println("\n[+] Ignored CSRF_TOKEN For:\n\n" + Arrays.toString(mappings) + "\n");
		http.csrf((csrf) -> csrf.ignoringRequestMatchers(mappings));
		http.headers((headers) -> headers.contentSecurityPolicy((policy) -> policy.policyDirectives(csPolicy)))
				.addFilterBefore(new CSPNonceFilter(), HeaderWriterFilter.class);
		return http.build();
	}

}
