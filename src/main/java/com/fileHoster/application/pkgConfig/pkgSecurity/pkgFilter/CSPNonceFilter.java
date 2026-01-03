/* (C) 2025 */
package com.fileHoster.application.pkgConfig.pkgSecurity.pkgFilter;

import com.fileHoster.application.pkgConfig.pkgProperties.ConfigClass;
import com.fileHoster.application.pkgConfig.pkgProperties.ConfigSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.filter.GenericFilterBean;

public class CSPNonceFilter extends GenericFilterBean {

	private static final int NONCE_SIZE = 32; // recommended = 128
	public static final String CSP_NONCE_ATTRIBUTE = "cspNonce";
	private static final String REQUEST_POLICY = "Content-Security-Policy";

	private static String nonceAttribute;

	private static SecureRandom secureRandom;

	@Autowired
	public CSPNonceFilter() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
		nonceAttribute = context.getBean(ConfigClass.CONFIG_BEAN, ConfigSet.class).getApiCspNonce();
		secureRandom = new SecureRandom();
		context.close();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		byte[] nonceArray = new byte[NONCE_SIZE];

		secureRandom.nextBytes(nonceArray);

		String nonce = Base64.getEncoder().encodeToString(nonceArray);
		request.setAttribute(CSP_NONCE_ATTRIBUTE, nonce);

		chain.doFilter(request, new CSPNonceResponseWrapper(response, nonce));
	}

	/**
	 * Wrapper to fill the nonce value
	 */
	private static class CSPNonceResponseWrapper extends HttpServletResponseWrapper {
		private final String nonce;

		public CSPNonceResponseWrapper(HttpServletResponse response, String nonce) {
			super(response);
			this.nonce = nonce;
		}

		@Override
		public void setHeader(String name, String value) {
			if (name.equals(REQUEST_POLICY) && !value.isBlank()) {
				super.setHeader(name, value.replace(nonceAttribute, nonce));
			} else {
				super.setHeader(name, value);
			}
		}

		@Override
		public void addHeader(String name, String value) {
			if (name.equals(REQUEST_POLICY) && !value.isBlank()) {
				super.addHeader(name, value.replace(nonceAttribute, nonce));
			} else {
				super.addHeader(name, value);
			}
		}
	}

}
