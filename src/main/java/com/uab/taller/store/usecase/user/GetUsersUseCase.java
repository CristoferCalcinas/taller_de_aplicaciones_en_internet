package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.response.UserSummaryResponse;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUsersUseCase {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMappingUseCase userMappingUseCase;

    /**
     * Obtiene todos los usuarios activos
     */
    public List<UserSummaryResponse> getAllUsers() {
        List<User> users = userService.findAll();
        // Filtrar usuarios no eliminados
        List<User> activeUsers = users.stream()
                .filter(user -> !user.isDeleted())
                .toList();

        return userMappingUseCase.toUserSummaryResponseList(activeUsers);
    }

    /**
     * Obtiene todos los usuarios incluyendo eliminados (para administradores)
     */
    public List<UserSummaryResponse> getAllUsersIncludingDeleted() {
        List<User> users = userService.findAll();
        return userMappingUseCase.toUserSummaryResponseList(users);
    }
}
