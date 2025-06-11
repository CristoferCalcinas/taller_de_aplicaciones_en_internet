package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.UpdateUserRequest;
import com.uab.taller.store.domain.dto.response.UserResponse;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IProfileService;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateUserUseCase {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProfileService profileService;

    @Autowired
    private UserMappingUseCase userMappingUseCase;

    @Transactional
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        // Buscar el usuario existente
        User existingUser = userService.findById(userId);
        if (existingUser == null) {
            throw new EntityNotFoundException("Usuario", userId);
        }

        // Actualizar datos del usuario
        updateUserFields(existingUser, request);

        // Actualizar datos del perfil si existe
        if (existingUser.getProfile() != null && hasProfileUpdates(request)) {
            updateProfileFields(existingUser.getProfile(), request);
            profileService.update(existingUser.getProfile());
        }

        // Guardar usuario actualizado
        User updatedUser = userService.update(existingUser);

        return userMappingUseCase.toUserResponse(updatedUser);
    }

    private void updateUserFields(User user, UpdateUserRequest request) {
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user.setEmail(request.getEmail().trim());
        }

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(request.getPassword());
        }
    }

    private void updateProfileFields(Profile profile, UpdateUserRequest request) {
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            profile.setName(request.getName().trim());
        }

        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            profile.setLastName(request.getLastName().trim());
        }

        if (request.getCi() != null && !request.getCi().trim().isEmpty()) {
            profile.setCi(request.getCi().trim());
        }

        if (request.getMobile() != null && !request.getMobile().trim().isEmpty()) {
            profile.setMobile(request.getMobile().trim());
        }

        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            profile.setAddress(request.getAddress().trim());
        }

        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            profile.setStatus(request.getStatus().trim());
        }
    }

    private boolean hasProfileUpdates(UpdateUserRequest request) {
        return request.getName() != null ||
                request.getLastName() != null ||
                request.getCi() != null ||
                request.getMobile() != null ||
                request.getAddress() != null ||
                request.getStatus() != null;
    }
}
