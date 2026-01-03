/* (C) 2025 */
package com.fileHoster.application.pkgDB.pkgControl;

import com.fileHoster.application.pkgDB.pkgEntity.FileEntry;
import com.fileHoster.application.pkgDB.pkgRepo.FileEntryRepository;
import com.fileHoster.application.pkgData.ProcessData;
import com.fileHoster.application.pkgRecord.Result;
import com.fileHoster.application.pkgRecord.Status;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@PropertySource("${application.configFile}")
@RequestMapping(path = "/${api.endPoint:${config.api}}")
public class DBController {

	@Value("${config.defaultValue}")
	private String defaultValue;

	@Value("${config.apiKey}")
	private String apiKey;

	@Value("${api.fileName}")
	private String apiFileName;

	@Value("${api.newFileName}")
	private String apiNewFileName;

	private static FileEntryRepository globFileEntryRepository;

	public static FileEntryRepository getGlobFileEntryRepository() {
		return globFileEntryRepository;
	}

	@Autowired
	private void setGlobFileEntryRepository(FileEntryRepository globFileEntryRepository) {
		DBController.globFileEntryRepository = globFileEntryRepository;
	}

	@PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
	@PostMapping("${api.getAllFiles}")
	public @ResponseBody Result getEntryList(@RequestBody Map<String, Object> body,
			@Param("request") HttpServletRequest request) {
		if (body.containsKey(apiKey)) {
			String apiKeyVal = String.valueOf(body.get(apiKey));
			if (ProcessData.compareAPIKey(apiKeyVal)) {
				HashMap<String, Map.Entry<String, Integer>> map = new HashMap<>();
				globFileEntryRepository.findAll().forEach(e -> {
					String fileType;
					try {
						fileType = URLConnection
								.guessContentTypeFromStream(new ByteArrayInputStream(e.getEntryFileBytes()));
						fileType = fileType == null ? defaultValue : fileType;
					} catch (IOException ex) {
						fileType = defaultValue;
					}
					map.put(e.getEntryID(), Map.entry(fileType, e.getEntryFileBytes().length));
				});
				return new Result(map);
			}
		}
		return new Result(defaultValue);
	}

	@PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
	@PostMapping("${api.renameFile}")
	public @ResponseBody Status renameEntry(@RequestBody Map<String, Object> body,
			@Param("request") HttpServletRequest request) {
		if (body.containsKey(apiKey) && body.containsKey(apiFileName) && body.containsKey(apiNewFileName)) {
			String apiKeyVal = String.valueOf(body.get(apiKey));
			if (ProcessData.compareAPIKey(apiKeyVal)) {
				String apiRenameFileNameVal = String.valueOf(body.get(apiFileName));
				String apiNewFileNameVal = String.valueOf(body.get(apiNewFileName));
				if (globFileEntryRepository.findById(apiRenameFileNameVal).isPresent()) {
					byte[] fileBytes = globFileEntryRepository.findById(apiRenameFileNameVal).get().getEntryFileBytes();
					globFileEntryRepository.deleteById(apiRenameFileNameVal);
					FileEntry fileEntry = new FileEntry();
					fileEntry.setEntryID(apiNewFileNameVal);
					fileEntry.setEntryFileBytes(fileBytes);
					globFileEntryRepository.save(fileEntry);
					return new Status(1);
				}
			}
		}
		return new Status(0);
	}

	@PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
	@PostMapping("${api.deleteFile}")
	public @ResponseBody Status deleteEntry(@RequestBody Map<String, Object> body,
			@Param("request") HttpServletRequest request) {
		if (body.containsKey(apiKey) && body.containsKey(apiFileName)) {
			String apiKeyVal = String.valueOf(body.get(apiKey));
			if (ProcessData.compareAPIKey(apiKeyVal)) {
				String apiDeleteFileNameVal = String.valueOf(body.get(apiFileName));
				if (globFileEntryRepository.existsById(apiDeleteFileNameVal)) {
					globFileEntryRepository.deleteById(apiDeleteFileNameVal);
					return new Status(1);
				}
			}
		}
		return new Status(0);
	}

	@PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
	@PostMapping("${api.deleteAllFiles}")
	public @ResponseBody Status deleteEntryList(@RequestBody Map<String, Object> body,
			@Param("request") HttpServletRequest request) {
		if (body.containsKey(apiKey)) {
			String apiKeyVal = String.valueOf(body.get(apiKey));
			if (ProcessData.compareAPIKey(apiKeyVal)) {
				globFileEntryRepository.deleteAll();
				return new Status(1);
			}
		}
		return new Status(0);
	}

}
