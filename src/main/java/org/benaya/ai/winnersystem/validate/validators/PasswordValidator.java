package org.benaya.ai.winnersystem.validate.validators;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.benaya.ai.winnersystem.validate.annotations.ValidPassword;
public class PasswordValidator implements ConstraintValidator<ValidPassword, String>{
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null || password.isEmpty()) {
            return true;
        }
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }
}