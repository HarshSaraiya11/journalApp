package com.myjournalApp.journalApp.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.myjournalApp.journalApp.entity.User;
import com.myjournalApp.journalApp.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                   PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
}

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public boolean login(String username, String rawPassword) {
        User foundUser = userRepository.findByUserName(username);
        if (foundUser == null) {
            return false;
        }
        System.out.println("Raw: " + rawPassword);
        System.out.println("Stored: " + foundUser.getPassword());
        System.out.println(passwordEncoder.matches(rawPassword, foundUser.getPassword()));//debugging
        return passwordEncoder.matches(
            rawPassword,
            foundUser.getPassword()
        );
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); 
         userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);

    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public void updateUser(String username, User updatedUser) {
    User existing = userRepository.findByUserName(username);
    if (existing == null) {
        throw new RuntimeException("User not found");
    }
    existing.setUserName(updatedUser.getUserName());
    existing.setEmail(updatedUser.getEmail());
    if (updatedUser.getPassword() != null &&
        !updatedUser.getPassword().isBlank()) {

        existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    }
    userRepository.save(existing);
}
}