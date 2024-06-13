package ru.mingazoff.SpringProject.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mingazoff.SpringProject.models.Person;
import ru.mingazoff.SpringProject.models.Role;
import ru.mingazoff.SpringProject.security.PersonDetails;
import ru.mingazoff.SpringProject.services.PeopleService;
import ru.mingazoff.SpringProject.util.PersonValidator;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonValidator personValidator;
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PersonValidator personValidator, PeopleService peopleService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
    }

    @GetMapping("/info")
    public String info(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person personFromPrincipal = personDetails.getPerson();

        Person person = peopleService.findOne(personFromPrincipal.getId()); // для обновления после изменеия в БД
        model.addAttribute("person", person);

        return "people/info";
    }


    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person personFromPrincipal = personDetails.getPerson();

        Person person = peopleService.findOne(id);

        if ((personFromPrincipal.getId() == id) |
                (personFromPrincipal.getRole().equals(Role.ROLE_ADMIN))) {
            model.addAttribute("person", person);
            return "people/update";
        }
        return "redirect:/people/info";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "/people/update";
        }
        Person updatedPerson = peopleService.findOne(id);

        if (!person.getEmail().equalsIgnoreCase(updatedPerson.getEmail())) {
            personValidator.validate(person, bindingResult);
            if (bindingResult.hasErrors()) {
                return "/people/update";
            }
        }

        updatedPerson.setUsername(person.getUsername());
        updatedPerson.setEmail(person.getEmail());

        peopleService.update(updatedPerson.getId(), updatedPerson);

        return "/people/sucupdate";
    }

    @GetMapping("/changepass/{id}")
    public String changePassForm(@PathVariable("id") int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person personFromPrincipal = personDetails.getPerson();

        Person person = peopleService.findOne(id);

        if ((personFromPrincipal.getId() == id) |
                (personFromPrincipal.getRole().equals(Role.ROLE_ADMIN))) {
            model.addAttribute("person", person);
            return "people/changepass";
        }

        return "redirect:/people/info";
    }

    @PostMapping("/changepass/{id}")
    public String changePass(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasFieldErrors("password")) {
            return "/people/changepass";
        }
        Person updatedPerson = peopleService.findOne(id);
        updatedPerson.setPassword(person.getPassword());
        peopleService.updatePass(updatedPerson.getId(), updatedPerson);
        return "/people/succhangepass";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person personFromPrincipal = personDetails.getPerson();

        if (personFromPrincipal.getId() == id) {
            peopleService.delete(id);
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            return "redirect:/";
        }

        if (personFromPrincipal.getRole().equals(Role.ROLE_ADMIN)) {
            peopleService.delete(id);
            return "redirect:/admin/viewall";
        } else return "redirect:/people/info";
    }
}
