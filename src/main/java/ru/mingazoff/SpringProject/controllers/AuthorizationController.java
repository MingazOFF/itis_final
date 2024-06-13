package ru.mingazoff.SpringProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mingazoff.SpringProject.services.PeopleService;
import ru.mingazoff.SpringProject.util.PersonValidator;


@Controller
@RequestMapping("/auth")
public class AuthorizationController {

    private final PersonValidator personValidator;

    private final PeopleService peopleService;

    @Autowired
    public AuthorizationController(PersonValidator personValidator, PeopleService peopleService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/suclogin")
    public String successfulloginPage() {
        return "auth/suclogin";
    }


}
