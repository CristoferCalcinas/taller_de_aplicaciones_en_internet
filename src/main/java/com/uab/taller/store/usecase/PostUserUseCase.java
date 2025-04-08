package com.uab.taller.store.usecase;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.UserRequest;
import com.uab.taller.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostUserUseCase {
    @Autowired
    private IUserService userService;

    public User save(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setLastName(userRequest.getEmail());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        return userService.save(user);
    }

}
