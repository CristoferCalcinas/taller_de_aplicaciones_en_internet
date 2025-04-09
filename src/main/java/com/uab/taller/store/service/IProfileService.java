package com.uab.taller.store.service;

import com.uab.taller.store.domain.Profile;

import java.util.List;

public interface IProfileService {
    Profile save(Profile profile);
    List<Profile> getAll();
    Profile getById(Long id);
    void deleteById(Long id);
    Profile update(Profile profile);
}
