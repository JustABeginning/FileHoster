/* (C) 2025 */
package com.fileHoster.application.pkgForm.pkgValidator.validation;

import com.fileHoster.application.pkgForm.pkgValidator.annotation.FileValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileValidator implements ConstraintValidator<FileValidation, MultipartFile> {

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		boolean res = false;
		context.disableDefaultConstraintViolation();

		if (file.isEmpty())
			context.buildConstraintViolationWithTemplate("{FileValidation.EmptyMessage}").addConstraintViolation();
		else
			res = true;

		return res;
	}
}
