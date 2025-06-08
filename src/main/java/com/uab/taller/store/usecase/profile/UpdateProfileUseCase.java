package com.uab.taller.store.usecase.profile;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.domain.dto.request.UpdateProfileRequest;
import com.uab.taller.store.service.interfaces.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfileUseCase {

    @Autowired
    private IProfileService profileService;

    @Autowired
    private ProfileMappingUseCase profileMappingUseCase;

    /**
     * Actualiza un perfil completo (usado por el controlador)
     */
    public Profile update(Profile profile) {
        if (profile == null || profile.getId() == null) {
            throw new IllegalArgumentException("El perfil y su ID no pueden ser nulos");
        }
        // Verificar que el perfil existe
        Profile existingProfile = profileService.findById(profile.getId());
        if (existingProfile == null) {
            throw new IllegalArgumentException("El perfil con ID " + profile.getId() + " no existe");
        }

        return profileService.update(profile);
    }

    /**
     * Actualiza un perfil usando UpdateProfileRequest con validaciones
     */
    public Profile updateWithRequest(Long profileId, UpdateProfileRequest request) {
        // Obtener el perfil existente
        Profile existingProfile = profileService.findById(profileId);
        if (existingProfile == null) {
            throw new IllegalArgumentException("El perfil con ID " + profileId + " no existe");
        }

        // Actualizar solo los campos proporcionados
        Profile updatedProfile = profileMappingUseCase.updateEntity(existingProfile, request);

        // Guardar los cambios
        return profileService.update(updatedProfile);
    }
}
