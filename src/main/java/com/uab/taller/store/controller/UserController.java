package com.uab.taller.store.controller;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.CreateUserRequest;
import com.uab.taller.store.domain.dto.request.GetUserByEmailRequest;
import com.uab.taller.store.domain.dto.request.UserLoginRequest;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.usecase.user.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
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

    @Autowired
    GetUserByEmailUseCase getUserByEmailUseCase;

    @Autowired
    UserLoginUseCase userLoginUseCase;

    @Operation(summary = "todos los usuarios del store")
    @GetMapping
    public List<User> getAll() {
        return getUsersUseCase.getAllUsers();
    }

    @Operation(summary = "obtiene al usuario por id")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return getUserUseCase.getByUserId(id);
    }

    @Operation(summary = "elimina un usuario por id")
    @DeleteMapping("/{id}")
    public DeleteResponse deleteById(@PathVariable Long id) {
        return deleteUserUseCase.deleteUserById(id);
    }

    @Operation(summary = "elimina permanentemente un usuario por id (solo administradores)")
    @DeleteMapping("/{id}/force")
    public DeleteResponse forceDeleteById(@PathVariable Long id) {
        return deleteUserUseCase.forceDeleteUserById(id);
    }

    @Operation(summary = "crea un usuario")
    @PostMapping
    public User save(@RequestBody CreateUserRequest createUserRequest) {
        return createUserUseCase.save(createUserRequest);
    }

    @Operation(summary = "obtiene el usuario por email")
    @PostMapping(value = "/email")
    public User getByEmail(@RequestBody GetUserByEmailRequest getUserByEmailRequest) {
        return getUserByEmailUseCase.execute(getUserByEmailRequest.getEmail());
    }

    @PostMapping(value = "/login")
    public User login(@RequestBody UserLoginRequest userLoginRequest) {
        return userLoginUseCase.getUserLogin(userLoginRequest);
    }
}
