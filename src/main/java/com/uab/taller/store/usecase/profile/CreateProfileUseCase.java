package com.uab.taller.store.usecase.profile;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.domain.dto.request.ProfileRequest;
import com.uab.taller.store.service.interfaces.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateProfileUseCase {

    @Autowired
    private IProfileService profileService;

    @Autowired
    private ProfileMappingUseCase profileMappingUseCase;

    public Profile save(ProfileRequest profileRequest) {
        // Usar el mapping service para convertir el DTO a entidad
        Profile profile = profileMappingUseCase.toProfile(profileRequest);

        // Establecer valores por defecto
        if (profile.getStatus() == null || profile.getStatus().trim().isEmpty()) {
            profile.setStatus("ACTIVE");
        }

        // Guardar el perfil
        return profileService.save(profile);
    }
}
