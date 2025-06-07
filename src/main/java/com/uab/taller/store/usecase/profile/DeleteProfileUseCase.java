package com.uab.taller.store.usecase.profile;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteProfileUseCase {
    @Autowired
    private IProfileService profileService;

    @Transactional
    public DeleteResponse deleteById(Long id) {
        // Validar que el ID no sea nulo
        if (id == null) {
            throw new IllegalArgumentException("El ID del perfil no puede ser nulo");
        }

        // Verificar que el perfil existe
        Profile profile = profileService.findById(id);
        if (profile == null) {
            throw new EntityNotFoundException("Perfil", id);
        }

        try {
            // Realizar soft delete
            profile.setDeleted(true);
            profile.setChangeUser("SYSTEM_DELETE");
            profileService.update(profile);

            return DeleteResponse.success(id, "Perfil");
        } catch (Exception e) {
            throw new EntityDeletionException("Perfil", id,
                    "Error interno durante la eliminación: " + e.getMessage());
        }
    }

    // Método para eliminación física
    @Transactional
    public DeleteResponse forceDeleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del perfil no puede ser nulo");
        }

        Profile profile = profileService.findById(id);
        if (profile == null) {
            throw new EntityNotFoundException("Perfil", id);
        }

        try {
            profileService.deleteById(id);
            return DeleteResponse.success(id, "Perfil");
        } catch (Exception e) {
            throw new EntityDeletionException("Perfil", id,
                    "Error durante la eliminación física: " + e.getMessage());
        }
    }
}
