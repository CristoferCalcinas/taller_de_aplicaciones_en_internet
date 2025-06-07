package com.uab.taller.store.service;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.repository.ProfileRepository;
import com.uab.taller.store.service.interfaces.IProfileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImp implements IProfileService {
    ProfileRepository profileRepository;

    public ProfileServiceImp(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    @Override
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public Profile findById(Long id) {
        return profileRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteById(Long id) {
        profileRepository.deleteById(id);
    }

    @Override
    public Profile update(Profile profile) {
        if (profile == null || profile.getId() == null) {
            throw new IllegalArgumentException("Perfil o ID del perfil no puede ser nulo");
        }
        Profile oldProfile = findById(profile.getId());
        oldProfile.setName(profile.getName());
        oldProfile.setLastName(profile.getLastName());
        // oldProfile.setBirthDate(profile.getBirthDate());
        // oldProfile.setGender(profile.getGender());
        return profileRepository.save(oldProfile);
    }
}
