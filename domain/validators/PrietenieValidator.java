package com.example.guiex1.domain.validators;


import com.example.guiex1.domain.Prietenie;

import java.util.Objects;

public class PrietenieValidator implements  Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(Objects.equals(entity.getIdPrieten1(), entity.getIdPrieten2()))
            throw new ValidationException("Id-urile celor doi prieteni trebuie sa fie diferite!");
    }
}
