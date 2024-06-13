package ru.mingazoff.SpringProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mingazoff.SpringProject.models.Person;
import ru.mingazoff.SpringProject.models.Role;
import ru.mingazoff.SpringProject.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Person> findByEmail(String email) {
        Optional<Person> personOptional = peopleRepository.findByEmail(email);
        return personOptional;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll(Sort.by("id"));

    }

    public Person findOne(int id) {
        Optional<Person> personOptional = peopleRepository.findById(id);
        return personOptional.orElse(null);
    }

    @Transactional
    public void register(Person person) {
        person.setEmail(person.getEmail().toLowerCase());
        person.setRole(Role.ROLE_USER);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        updatedPerson.setEmail(updatedPerson.getEmail().toLowerCase());
        peopleRepository.save(updatedPerson);

    }@Transactional
    public void updatePass(int id, Person updatedPerson) {
        updatedPerson.setId(id);

        updatedPerson.setPassword(passwordEncoder.encode(updatedPerson.getPassword()));
        peopleRepository.save(updatedPerson);

    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
