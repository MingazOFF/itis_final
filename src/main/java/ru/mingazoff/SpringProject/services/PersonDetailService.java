package ru.mingazoff.SpringProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mingazoff.SpringProject.models.Person;
import ru.mingazoff.SpringProject.repositories.PeopleRepository;
import ru.mingazoff.SpringProject.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailService implements UserDetailsService {
    private final PeopleRepository peopleRepository;


    @Autowired
    public PersonDetailService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Person> personOptional = peopleRepository.findByEmail(email.toLowerCase());
        if (personOptional.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь с указанным email не найден");
        }
        return new PersonDetails(personOptional.get());
    }
}
