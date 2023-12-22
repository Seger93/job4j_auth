package ru.job4j.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePersonService implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(Integer integer) {
        return personRepository.findById(integer);
    }

    @Override
    public Optional<Person> save(Person person) {
        if ( personRepository.findByLogin(person.getLogin()).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(personRepository.save(person));
    }

    @Override
    public boolean delete(Integer id) {
        var findPerson = personRepository.findById(id);
        if (findPerson.isPresent()) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Person person) {
        var findPerson = personRepository.findById(person.getId());
        if (findPerson.isPresent()) {
            personRepository.save(person);
            return true;
        }
        return false;
    }
}