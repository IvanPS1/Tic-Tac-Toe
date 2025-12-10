package com.tictactoe.domain.service;

import com.tictactoe.domain.model.User;
import com.tictactoe.datasource.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(String login, String password) {
        if (userRepository.findByLogin(login).isPresent()) {
            return false;
        }
        User user = new User(login, password);
        userRepository.save(user);
        return true;
    }

    public Optional<UUID> authenticate(String login, String password) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent() && user.get().password.equals(password)) {
            return Optional.of(user.get().id);
        }
        return Optional.empty();
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
}
