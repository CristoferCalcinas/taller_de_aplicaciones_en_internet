package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.response.UserResponse;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserUseCase {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMappingUseCase userMappingUseCase;

    /**
     * Obtiene un usuario por su ID
     */
    public UserResponse getByUserId(Long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("Usuario", id);
        }

        return userMappingUseCase.toUserResponse(user);
    }

    /**
     * Obtiene un usuario por su ID incluyendo usuarios eliminados
     */
    public UserResponse getByUserIdIncludingDeleted(Long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("Usuario", id);
        }

        return userMappingUseCase.toUserResponse(user);
    }
}
