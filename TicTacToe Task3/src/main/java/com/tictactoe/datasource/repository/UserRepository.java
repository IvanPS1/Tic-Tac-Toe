package com.tictactoe.datasource.repository;

import com.tictactoe.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByLogin(String login);
}