/* (C) 2025 */
package com.fileHoster.application.pkgController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@PropertySource("${application.configFile}")
public class ProjectErrorController implements ErrorController {

	@Value("${config.redirectURL}")
	private String redirectURL;

	@RequestMapping("/error")
	public RedirectView handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {

			int statusCode = Integer.parseInt(status.toString());

			String format = "--> error-";

			String stat = "\n[*]" + " " + request.getRemoteAddr() + " " + format + statusCode + "\n";

			System.out.println(stat);

		}

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(redirectURL);
		return redirectView;
	}

}
