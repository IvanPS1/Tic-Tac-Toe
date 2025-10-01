package com.tictactoe.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    @Column(unique = true, nullable = false)
    public String login;

    @Column(nullable = false)
    public String password;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}