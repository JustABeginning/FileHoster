/* (C) 2025 */
package com.fileHoster.application.pkgDB.pkgRepo;

import com.fileHoster.application.pkgDB.pkgEntity.FileEntry;
import org.springframework.data.repository.CrudRepository;

/* @Repository */
public interface FileEntryRepository extends CrudRepository<FileEntry, String> {

}
