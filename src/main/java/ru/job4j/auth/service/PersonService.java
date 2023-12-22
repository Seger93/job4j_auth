package ru.job4j.auth.service;

import ru.job4j.auth.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    List<Person> findAll();

    Optional<Person> findById(Integer integer);

    Optional<Person> save(Person person);

    boolean delete(Integer id);

    boolean update(Person person);
}