package com.example.guiex1.domain.validators;

import com.example.guiex1.domain.Utilizator;

import java.util.Objects;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException
    {
        if(Objects.equals(entity.getFirstName(), "") && Objects.equals(entity.getLastName(), ""))
            throw  new ValidationException("Prenumele si numele trebuie sa fie stringuri nenule!");
        if(Objects.equals(entity.getFirstName(), ""))
            throw new ValidationException("Prenumele trebuie sa fie un string nenul!");
        if(Objects.equals(entity.getLastName(), ""))
            throw new ValidationException("Numele trebuie sa fie un string nenul!");
    }
}
