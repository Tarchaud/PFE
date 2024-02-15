package com.pfe.wakfubuilder.service;

import org.springframework.stereotype.Service;
import com.pfe.wakfubuilder.model.User;
import com.pfe.wakfubuilder.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createUser(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);

        return true;
    }
}