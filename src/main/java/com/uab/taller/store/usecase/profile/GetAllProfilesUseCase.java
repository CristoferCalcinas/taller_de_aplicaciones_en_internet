package com.uab.taller.store.usecase.profile;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.service.interfaces.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllProfilesUseCase {
    @Autowired
    IProfileService profileService;

    public List<Profile> getAll() {
        return profileService.findAll();
    }
}
