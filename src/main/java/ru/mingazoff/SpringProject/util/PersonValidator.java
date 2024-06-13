package ru.mingazoff.SpringProject.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mingazoff.SpringProject.models.Person;
import ru.mingazoff.SpringProject.services.PeopleService;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        person.setEmail(person.getEmail().toLowerCase());
        Optional<Person> personOptional = peopleService.findByEmail(person.getEmail());
        if (personOptional.isEmpty())
            return;
        errors.rejectValue("email", "", "Аккаунт с таким email уже зарегистрирован");


    }
}
