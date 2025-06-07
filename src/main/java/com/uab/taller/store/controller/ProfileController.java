package com.uab.taller.store.controller;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.domain.dto.request.ProfileRequest;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.usecase.profile.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/profile")
public class ProfileController {
    @Autowired
    CreateProfileUseCase saveProfileUseCase;

    @Autowired
    GetAllProfilesUseCase getAllProfilesUseCase;

    @Autowired
    GetProfileByIdUseCase getProfileByIdUseCase;

    @Autowired
    DeleteProfileUseCase deleteProfileUseCase;

    @Autowired
    UpdateProfileUseCase updateProfileUseCase;

    @PostMapping
    public Profile saveProfile(@RequestBody ProfileRequest profileRequest) {
        return saveProfileUseCase.save(profileRequest);
    }

    @GetMapping
    public List<Profile> getAllProfiles() {
        return getAllProfilesUseCase.getAll();
    }

    @GetMapping("/{id}")
    public Profile getProfileById(@PathVariable Long id) {
        return getProfileByIdUseCase.getById(id);
    }

    @DeleteMapping("/{id}")
    public DeleteResponse deleteProfileById(@PathVariable Long id) {
        return deleteProfileUseCase.deleteById(id);
    }

    @DeleteMapping("/{id}/force")
    public DeleteResponse forceDeleteProfileById(@PathVariable Long id) {
        return deleteProfileUseCase.forceDeleteById(id);
    }

    @PutMapping
    public Profile updateProfile(@RequestBody Profile profile) {
        return updateProfileUseCase.update(profile);
    }
}
