package com.uab.taller.store.controller;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.domain.dto.request.RolRequest;
import com.uab.taller.store.usecase.rol.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/rol")
public class RolController {
    @Autowired
    CreateRolUseCase createRolUseCase;

    @Autowired
    GetAllRolesUseCase getAllRolesUseCase;

    @Autowired
    GetRolByIdUseCase getRolByIdUseCase;

    @Autowired
    GetRolByNameUseCase getRolByNameUseCase;

    @Autowired
    DeleteRolUseCase deleteRolUseCase;

    @Autowired
    UpdateRolUseCase updateRolUseCase;

    @PostMapping
    public Rol createRol(@RequestBody RolRequest rolRequest) {
        return createRolUseCase.save(rolRequest);
    }

    @GetMapping
    public List<Rol> getAllRoles() {
        return getAllRolesUseCase.getAllRoles();
    }

    @GetMapping("/{id}")
    public Rol getRolById(@PathVariable Long id) {
        return getRolByIdUseCase.getById(id);
    }

    @GetMapping("/name/{name}")
    public Rol getRolByName(@PathVariable String name) {
        return getRolByNameUseCase.getByName(name);
    }

    @DeleteMapping("/{id}")
    public void deleteRolById(@PathVariable Long id) {
        deleteRolUseCase.deleteById(id);
    }

    @PutMapping
    public Rol updateRol(@RequestBody Rol rol) {
        return updateRolUseCase.update(rol);
    }
}
