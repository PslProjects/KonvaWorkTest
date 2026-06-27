
package com.scada.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scada.entities.User;
import com.scada.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public User addUser(String username, String password, String designation, byte[] photo) {
        if (repo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        int maxSize = 10 * 1024 * 1024;

        if (photo != null && photo.length > maxSize) {
            throw new RuntimeException("Image limit exceeded. Max allowed size is 10MB");
        }

        String encodedPass = encoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPass);
        user.setDesignation(designation);
        user.setPhoto(photo);

        return repo.save(user);
    }

    public User addAdmin(String username, String password) {
        if (repo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        String encodedPass = encoder.encode(password);

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(encodedPass);
        admin.setRole("ADMIN");
        admin.setDesignation(null);
        admin.setPhoto(null);

        return repo.save(admin);
    }

    public User validateUser(String username, String password) {
        System.err.println("Validating user: " + username);
        System.err.println("Provided password: " + password);

        User user = repo.findByUsername(username)
                .orElse(null);

        if (user == null) return null;

        boolean match = encoder.matches(password, user.getPassword());
        return match ? user : null;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }
}
