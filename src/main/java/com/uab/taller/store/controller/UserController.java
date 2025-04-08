package com.uab.taller.store.controller;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.UserRequest;
import com.uab.taller.store.usecase.DeleteUserUseCase;
import com.uab.taller.store.usecase.GetUserUseCase;
import com.uab.taller.store.usecase.GetUsersUseCase;
import com.uab.taller.store.usecase.PostUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    GetUsersUseCase getUsersUseCase;

    @Autowired
    GetUserUseCase getUserUseCase;

    @Autowired
    DeleteUserUseCase deleteUserUseCase;

    @Autowired
    PostUserUseCase postUserUseCase;

    @GetMapping
    public List<User> getAll() {
        return getUsersUseCase.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
     return getUserUseCase.getByUserId(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        deleteUserUseCase.deleteUserById(id);
    }

    @PostMapping
    public User save(@RequestBody UserRequest userRequest) {
        return postUserUseCase.save(userRequest);
    }
}
