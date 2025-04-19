package com.uab.taller.store.controller;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.CreateUserRequest;
import com.uab.taller.store.usecase.user.DeleteUserUseCase;
import com.uab.taller.store.usecase.user.GetUserUseCase;
import com.uab.taller.store.usecase.user.GetUsersUseCase;
import com.uab.taller.store.usecase.user.CreateUserUseCase;
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
    CreateUserUseCase createUserUseCase;

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
    public User save(@RequestBody CreateUserRequest createUserRequest) {
        return createUserUseCase.save(createUserRequest);
    }
}
