package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllRolesUseCase {
    @Autowired
    private IRolService rolService;

    public List<Rol> getAllRoles() {
        return rolService.findAll();
    }
}
