package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteRolUseCase {
    @Autowired
    private IRolService rolService;

    public void deleteById(Long id) {
        rolService.deleteById(id);
    }
}
