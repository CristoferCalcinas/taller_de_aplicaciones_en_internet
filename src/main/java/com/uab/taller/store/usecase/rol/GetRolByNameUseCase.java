package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetRolByNameUseCase {
    @Autowired
    private RolRepository rolRepository;

    public Rol getByName(String name) {
        return rolRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Rol not found with name: " + name));
    }
}
