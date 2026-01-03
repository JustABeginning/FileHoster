/* (C) 2025 */
package com.fileHoster.application.pkgController;

import com.fileHoster.application.pkgConfig.pkgProperties.ConfigClass;
import com.fileHoster.application.pkgConfig.pkgSecurity.pkgFilter.CSPNonceFilter;
import com.fileHoster.application.pkgDB.pkgControl.DBController;
import com.fileHoster.application.pkgDB.pkgEntity.FileEntry;
import com.fileHoster.application.pkgData.ProcessData;
import com.fileHoster.application.pkgForm.pkgModel.Form;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
// @PropertySource("${application.configFile}")
public class RequestController {

	private static final String FORM_OBJECT = "formObject";
	private static final String NONCE_ATTRIBUTE = "nonce";

	@PreAuthorize("#request.getRemoteAddr().equals('" + ConfigClass.LOCALHOST + "')")
	@GetMapping(ConfigClass.API_HOME)
	public String indexForm(Model model, @Param("request") HttpServletRequest request) {
		model.addAttribute(FORM_OBJECT, new Form());
		model.addAttribute(NONCE_ATTRIBUTE, request.getAttribute(CSPNonceFilter.CSP_NONCE_ATTRIBUTE));
		return "main/index";
	}

	@PreAuthorize("#request.getRemoteAddr().equals('" + ConfigClass.LOCALHOST + "')")
	@PostMapping(ConfigClass.API_HOME)
	public String indexSubmit(Model model, @Valid @ModelAttribute(FORM_OBJECT) Form formObj,
			BindingResult bindingResult, @Param("request") HttpServletRequest request) {
		if (!bindingResult.hasErrors()) {
			if (ProcessData.compareAPIKey(formObj.getApiKey())) {
				if (DBController.getGlobFileEntryRepository().existsById(formObj.getFileName()))
					DBController.getGlobFileEntryRepository().deleteById(formObj.getFileName());
				FileEntry entry = new FileEntry();
				entry.setEntryID(formObj.getFileName());
				try {
					entry.setEntryFileBytes(formObj.getFile().getBytes());
				} catch (IOException e) {
					entry.setEntryFileBytes(new byte[]{});
				}
				DBController.getGlobFileEntryRepository().save(entry);
			}
		}
		model.addAttribute(NONCE_ATTRIBUTE, request.getAttribute(CSPNonceFilter.CSP_NONCE_ATTRIBUTE));
		return "main/index";
	}

	@GetMapping(path = "/{pathMap}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	@ResponseBody
	public ResponseEntity<?> downloadFile(@PathVariable("pathMap") String pathVal) {
		if (DBController.getGlobFileEntryRepository().existsById(pathVal)) {
			if (DBController.getGlobFileEntryRepository().findById(pathVal).isPresent()) {
				byte[] fileBytes = DBController.getGlobFileEntryRepository().findById(pathVal).get()
						.getEntryFileBytes();
				Resource resource = new ByteArrayResource(fileBytes);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
