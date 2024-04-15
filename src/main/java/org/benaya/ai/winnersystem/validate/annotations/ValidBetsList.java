package org.benaya.ai.winnersystem.validate.annotations;

import jakarta.validation.Constraint;
import org.benaya.ai.winnersystem.validate.validators.BetsListValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BetsListValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBetsList {
}
