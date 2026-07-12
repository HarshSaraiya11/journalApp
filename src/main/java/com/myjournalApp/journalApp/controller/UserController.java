package com.myjournalApp.journalApp.controller;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.myjournalApp.journalApp.entity.User;
import com.myjournalApp.journalApp.service.UserService;
import com.myjournalApp.journalApp.security.JwtUtil;;

@RestController
@RequestMapping("/user")
public class UserController {
    
    private UserService userService;
    private JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil){
        this.userService=userService;
        this.jwtUtil=jwtUtil;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    } 

    @PostMapping("/create")
    public void createUser(@RequestBody User user){
        userService.saveUser(user);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginEntity(@RequestBody User user) {
        boolean valid = userService.login(
        user.getUserName(),
        user.getPassword()        
        );
        if (!valid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = jwtUtil.generateToken(user.getUserName());
        
        return ResponseEntity.ok(token);
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user, Authentication authentication){
       String username = authentication.getName();
       userService.updateUser(username,user);
       return ResponseEntity.ok().build();
    }
}
