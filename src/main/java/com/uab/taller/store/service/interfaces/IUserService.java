package com.uab.taller.store.service.interfaces;

import com.uab.taller.store.domain.User;

import java.util.Optional;

public interface IUserService extends IGenericRepository<User> {
    Optional<User> getByEmail(String email);
}
