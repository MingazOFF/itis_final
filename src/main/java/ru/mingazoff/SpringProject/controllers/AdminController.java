package ru.mingazoff.SpringProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mingazoff.SpringProject.models.Person;
import ru.mingazoff.SpringProject.models.Role;
import ru.mingazoff.SpringProject.services.PeopleService;



@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PeopleService peopleService;

    @Autowired
    public AdminController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/viewall")
    public String viewAll(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "admin/viewall";
    }

    @GetMapping("/changerole/{id}")
    public String changeRole(@PathVariable("id") int id) {
        Person person = peopleService.findOne(id);

        if(person.getRole().equals(Role.ROLE_USER)) {
            person.setRole(Role.ROLE_ADMIN);
        } else {
            person.setRole(Role.ROLE_USER);
        }

        peopleService.update(id, person);
        return "redirect:/admin/viewall";
    }
}
