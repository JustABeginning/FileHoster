/* (C) 2025 */
package com.fileHoster.application.pkgForm.pkgValidator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.fileHoster.application.pkgForm.pkgValidator.validation.FileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = FileValidator.class)
public @interface FileValidation {
	String message() default "{FileValidation.DefaultMessage}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
