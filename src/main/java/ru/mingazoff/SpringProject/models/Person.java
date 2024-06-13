package ru.mingazoff.SpringProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "Person")
@ToString
@Getter
@Setter
public class Person {

    public Person() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Имя не может состоять из пробелов")
    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    @Column(name = "username")
    private String username;


    @Email(message = "не соответствует формату email: user@mail.ru")
    @Column(name = "email")
    private String email;

    //@NotEmpty(message = "пароль не может быть пустым")
    @Pattern(regexp ="(?=.*[0-9])(?=.*[a-zA-Z]).{6,}"  , message = "пароль должен состоять минимум из 6 символов, включать цифру(-ы) и латинскую букву(-ы)")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;


}
