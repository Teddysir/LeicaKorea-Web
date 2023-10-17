package com.example.leica_refactoring.Auth.service;

import com.example.leica_refactoring.Auth.annotation.Authorities;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

public class AuthoritiesValidator implements ConstraintValidator<Authorities,String[]> {

    @Override
    public boolean isValid(String[] authorities, ConstraintValidatorContext context) {
        if(authorities == null || authorities.length == 0) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("not null",null)
            ).addConstraintViolation();
            return false;
        }
        for(String authority : authorities) {
            if(authority.equals("ROLE_USER")) continue;

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("only ROLE_USER",null)
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
