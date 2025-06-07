package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateRolUseCase {
    @Autowired
    private IRolService rolService;

    public Rol update(Rol rol) {
        return rolService.update(rol);
    }
}
