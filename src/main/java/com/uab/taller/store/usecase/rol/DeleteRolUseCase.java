package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteRolUseCase {
    @Autowired
    private IRolService rolService;

    @Transactional
    public DeleteResponse deleteById(Long id) {
        // Validar que el ID no sea nulo
        if (id == null) {
            throw new IllegalArgumentException("El ID del rol no puede ser nulo");
        }

        // Verificar que el rol existe
        Rol rol = rolService.findById(id);
        if (rol == null) {
            throw new EntityNotFoundException("Rol", id);
        }

        // Validar que el rol no esté siendo usado por usuarios
        // En una implementación real, aquí verificarías si hay usuarios con este rol

        try {
            // Realizar soft delete
            rol.setDeleted(true);
            rol.setChangeUser("SYSTEM_DELETE");
            rolService.update(rol);

            return DeleteResponse.success(id, "Rol");
        } catch (Exception e) {
            throw new EntityDeletionException("Rol", id,
                    "Error interno durante la eliminación: " + e.getMessage());
        }
    }

    // Método para eliminación física
    @Transactional
    public DeleteResponse forceDeleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del rol no puede ser nulo");
        }

        Rol rol = rolService.findById(id);
        if (rol == null) {
            throw new EntityNotFoundException("Rol", id);
        }

        try {
            rolService.deleteById(id);
            return DeleteResponse.success(id, "Rol");
        } catch (Exception e) {
            throw new EntityDeletionException("Rol", id,
                    "Error durante la eliminación física: " + e.getMessage());
        }
    }
}
