package ru.job4j.auth.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Table(name = "person")
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Не может быть Null")
    private int id;
    @NotEmpty(message = "Не может быть пустым полем")
    private String login;
    @NotBlank(message = "Не может быть пустым полем")
    @Size(min = 2, max = 10)
    private String password;
}