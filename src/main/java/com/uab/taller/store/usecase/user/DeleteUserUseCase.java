package com.uab.taller.store.usecase.user;

import com.uab.taller.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserUseCase {
    @Autowired
    private IUserService userService;

    public void deleteUserById(Long id) {
        userService.deleteById(id);
    }

}
