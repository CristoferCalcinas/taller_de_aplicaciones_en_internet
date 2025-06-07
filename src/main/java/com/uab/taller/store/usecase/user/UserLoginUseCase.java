package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.UserLoginRequest;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserLoginUseCase {
    @Autowired
    IUserService userService;

    public  User getUserLogin(UserLoginRequest userLoginUseCase) {
        Optional<User> optionalUser = userService.getByEmail(userLoginUseCase.getEmail());

        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(userLoginUseCase.getPassword())) {
            return optionalUser.get();
        } else {
            return null;
        }
    }
}
