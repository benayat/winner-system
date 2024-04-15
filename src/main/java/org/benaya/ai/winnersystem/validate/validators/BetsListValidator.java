package org.benaya.ai.winnersystem.validate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.benaya.ai.winnersystem.model.dto.ClientBetDto;
import org.benaya.ai.winnersystem.validate.annotations.ValidBetsList;

import java.util.List;

public class BetsListValidator implements ConstraintValidator<ValidBetsList, List<ClientBetDto>> {

    @Override
    public boolean isValid(List<ClientBetDto> clientBets, ConstraintValidatorContext constraintValidatorContext) {
        return !clientBets.isEmpty();
    }

    @Override
    public void initialize(ValidBetsList constraintAnnotation) {
    }

}
