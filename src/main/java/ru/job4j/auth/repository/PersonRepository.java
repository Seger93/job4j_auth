package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.auth.model.Person;

import java.util.List;
import java.util.Set;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    List<Person> findAll();
}