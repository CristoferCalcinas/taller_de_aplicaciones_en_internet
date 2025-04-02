package com.uab.taller.store.controller;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.UserRequest;
import com.uab.taller.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    IUserService userService;


    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ;
    }

    @PostMapping
    public User create(@RequestBody UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setLastName(userRequest.getEmail());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        return userService.save(user);
    }

}
