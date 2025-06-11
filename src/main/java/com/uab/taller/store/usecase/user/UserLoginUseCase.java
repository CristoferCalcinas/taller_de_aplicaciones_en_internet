package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.UserLoginRequest;
import com.uab.taller.store.domain.dto.response.UserLoginResponse;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserLoginUseCase {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMappingUseCase userMappingUseCase;

    /**
     * Autentica un usuario con email y contraseña
     */
    public UserLoginResponse getUserLogin(UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new IllegalArgumentException("Los datos de login no pueden ser nulos");
        }

        if (userLoginRequest.getEmail() == null || userLoginRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (userLoginRequest.getPassword() == null || userLoginRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        Optional<User> optionalUser = userService.getByEmail(userLoginRequest.getEmail().trim());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Verificar si el usuario está eliminado
            if (user.isDeleted()) {
                throw new EntityNotFoundException("Usuario no encontrado o cuenta desactivada");
            }

            // Verificar si el perfil está inactivo
            if (user.getProfile() != null &&
                    "INACTIVE".equals(user.getProfile().getStatus()) ||
                    "SUSPENDED".equals(user.getProfile().getStatus())) {
                throw new IllegalStateException("La cuenta está " + user.getProfile().getStatus().toLowerCase());
            }

            // Verificar contraseña
            if (user.getPassword().equals(userLoginRequest.getPassword())) {
                // En un entorno real, aquí se debería actualizar la fecha de último acceso
                updateLastAccess(user);

                return userMappingUseCase.toUserLoginResponse(user);
            } else {
                throw new IllegalArgumentException("Credenciales inválidas");
            }
        } else {
            throw new EntityNotFoundException("Usuario no encontrado");
        }
    }

    /**
     * Actualiza la fecha de último acceso del usuario
     */
    private void updateLastAccess(User user) {
        try {
            // Actualizar changeDate como último acceso
            userService.update(user);
        } catch (Exception e) {
            // Log del error pero no fallar el login por esto
            System.err.println("Error actualizando último acceso: " + e.getMessage());
        }
    }
}
