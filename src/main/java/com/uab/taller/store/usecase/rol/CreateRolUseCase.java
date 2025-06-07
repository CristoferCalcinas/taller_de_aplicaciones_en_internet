package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.domain.dto.request.RolRequest;
import com.uab.taller.store.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateRolUseCase {
    @Autowired
    private IRolService rolService;

    public Rol save(RolRequest rolRequest) {
        Rol rol = new Rol();
        rol.setName(rolRequest.getName());
        rol.setDescription(rolRequest.getDescription());
        return rolService.save(rol);
    }
}
