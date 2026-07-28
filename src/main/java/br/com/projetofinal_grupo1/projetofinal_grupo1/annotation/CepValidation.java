package br.com.projetofinal_grupo1.projetofinal_grupo1.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {VerificacaoCep.class})
public @interface CepValidation {
    String message() default "Cep Inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
