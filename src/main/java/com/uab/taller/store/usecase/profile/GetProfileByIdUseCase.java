package com.uab.taller.store.usecase.profile;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.service.interfaces.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetProfileByIdUseCase {
    @Autowired
    IProfileService profileService;

    public Profile getById(Long id) {
        return profileService.findById(id);
    }
}
