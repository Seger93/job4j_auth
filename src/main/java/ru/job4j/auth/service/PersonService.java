package ru.job4j.auth.service;

import ru.job4j.auth.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    List<Person> findAll();

    Optional<Person> findById(Integer integer);

    Person save(Person person);

    void delete(Person person);
}