package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserUseCase {
    @Autowired
    IUserService userService;

    public User getByUserId(Long id) {
        return userService.findById(id);
    }

}
