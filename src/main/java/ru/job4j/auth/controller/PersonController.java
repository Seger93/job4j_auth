package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.dto.PersonDTO;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.SimplePersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());
    private final SimplePersonService persons;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder encoder;

    @GetMapping("/all")
    public ResponseEntity<List<Person>> findAll() {
        List<Person> allPersons = this.persons.findAll();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allPersons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.persons.findById(id);
        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не найден такой Id");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(person.get());
    }

    @PostMapping("/create")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        if (person.getPassword() == null || person.getLogin() == null) {
            throw new NullPointerException("Поле пароль или логин - пустое");
        }
        if (person.getPassword().length() < 2) {
            throw new IllegalArgumentException("Слишком короткий пароль");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        var savedPerson = this.persons.save(person);
        if (savedPerson.isEmpty()) {
            return new ResponseEntity<Person>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Person>(savedPerson.get(), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        if (person.getPassword() == null || person.getLogin() == null) {
            throw new NullPointerException("Поле пароль или логин - пустое");
        }
        if (person.getPassword().length() < 2) {
            throw new IllegalArgumentException("Слишком короткий пароль");
        }
        if (persons.update(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (persons.delete(id)) {
            return ResponseEntity.ok().build();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удален по такому ID");
    }

    @PatchMapping("/{id}")
    public Person example2(@RequestBody PersonDTO personDto, @PathVariable Integer id) {
        Optional<Person> person = persons.findById(id);
        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не найден такой Id");
        }
        person.get().setPassword(encoder.encode(personDto.getPassword()));
        if (person.get().getPassword() == null || person.get().getLogin() == null) {
            throw new NullPointerException("Поле пароль или логин - пустое");
        }
        if (person.get().getPassword().length() < 2) {
            throw new IllegalArgumentException("Слишком короткий пароль");
        }
        persons.update(person.get());
        return person.get();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
        LOGGER.error(e.getLocalizedMessage());
    }
}