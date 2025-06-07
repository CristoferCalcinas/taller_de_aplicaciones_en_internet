package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetRolByNameUseCase {
    @Autowired
    private IRolService rolService;

    public Rol getByName(String name) {
        return rolService.findAll().stream()
                .filter(rol -> rol.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Rol not found with name: " + name));
    }
}
