package com.uab.taller.store.usecase;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserUseCase {
    @Autowired
    IUserService userService;

    public User getByUserId(Long id) {
        return userService.getById(id);
    }

}
