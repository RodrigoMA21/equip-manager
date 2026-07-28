package br.com.projetofinal_grupo1.projetofinal_grupo1.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificacaoCep implements ConstraintValidator<CepValidation, String> {

    private static final String PATTERN_CEP = "^(([0-9]{2}\\.[0-9]{3}-[0-9]{3})|([0-9]{2}[0-9]{3}-[0-9]{3})|([0-9]{8}))$";

    @Override
    public void initialize(CepValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        boolean validation;

        if(cep == null || cep.isEmpty() || cep.equals(" ")){
            validation = false;
        }
        else {
            Pattern pattern = Pattern.compile(PATTERN_CEP);
            Matcher matcher = pattern.matcher(cep);
            validation = matcher.find();
        }
        return validation;
    }
}
