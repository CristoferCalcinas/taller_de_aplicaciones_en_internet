package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUsersUseCase {
    @Autowired
    IUserService userService;

    public List<User> getAllUsers() {
        return userService.findAll();
    }
}
