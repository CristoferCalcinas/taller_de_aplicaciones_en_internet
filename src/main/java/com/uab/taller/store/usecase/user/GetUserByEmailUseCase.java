package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.response.UserResponse;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUserByEmailUseCase {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMappingUseCase userMappingUseCase;

    /**
     * Busca un usuario por su email
     */
    public UserResponse execute(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }

        Optional<User> optionalUser = userService.getByEmail(email.trim());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isDeleted()) {
                throw new EntityNotFoundException("Usuario con email " + email + " no encontrado o está eliminado");
            }
            return userMappingUseCase.toUserResponse(user);
        } else {
            throw new EntityNotFoundException("Usuario con email " + email + " no encontrado");
        }
    }

    /**
     * Busca un usuario por su email incluyendo usuarios eliminados
     */
    public UserResponse executeIncludingDeleted(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }

        Optional<User> optionalUser = userService.getByEmail(email.trim());

        if (optionalUser.isPresent()) {
            return userMappingUseCase.toUserResponse(optionalUser.get());
        } else {
            throw new EntityNotFoundException("Usuario con email " + email + " no encontrado");
        }
    }
}
