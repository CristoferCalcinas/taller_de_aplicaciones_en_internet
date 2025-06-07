package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUserByEmailUseCase {
    @Autowired
    IUserService userService;

    public User execute(String email) {
        Optional<User> optionalUser =  userService.getByEmail(email);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }else {
            return null;
        }
    }

}
