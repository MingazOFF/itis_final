package ru.mingazoff.SpringProject.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mingazoff.SpringProject.models.Person;
import ru.mingazoff.SpringProject.services.PeopleService;
import ru.mingazoff.SpringProject.util.PersonValidator;

@Controller
@RequestMapping("/reg")
public class RegistrationController {

    private  final PersonValidator personValidator;
    private final PeopleService peopleService;

    public RegistrationController(PersonValidator personValidator, PeopleService peopleService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
    }
    @GetMapping("/reg")
    public String regForm (@ModelAttribute("person") Person person) {
        return "reg/reg";
    }

    @PostMapping("/reg")
    public String registration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        person.setEmail(person.getEmail().toLowerCase());
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "reg/reg";

        peopleService.register(person);
        return "reg/sucreg";
    }


}
