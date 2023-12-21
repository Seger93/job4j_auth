package ru.job4j.auth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.SimplePersonService;

import java.util.List;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final SimplePersonService persons;

    @GetMapping("/")
    public List<Person> findAll() {
        return this.persons.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.persons.findById(id);
        if (person.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(person.get());
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        var savedPerson = this.persons.save(person);
        if (savedPerson == null) {
            return new ResponseEntity<Person>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Person>(savedPerson, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<String> update(@RequestBody Person person) {
        try {
            this.persons.save(person);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка сохранения" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        try {
            Person person = new Person();
            person.setId(id);
            this.persons.delete(person);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка удаления" + e.getMessage());
        }
    }
}