package org.benaya.ai.winnersystem.validate.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.benaya.ai.winnersystem.validate.validators.PasswordValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Password must have at least one digit, one lowercase letter, one uppercase letter, one special character, no whitespace, and be at least 8 characters long.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}