package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.response.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMappingUseCase {

    /**
     * Convierte una entidad User a UserResponse
     */
    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .profile(user.getProfile() != null ? toProfileSummary(user) : null)
                .rol(user.getRol() != null ? toRolSummary(user) : null)
                .accounts(user.getAccounts() != null ? user.getAccounts().stream()
                        .filter(account -> !account.isDeleted())
                        .map(this::toAccountSummary)
                        .collect(Collectors.toList()) : null)
                .totalAccounts(user.getAccounts() != null
                        ? (int) user.getAccounts().stream().filter(account -> !account.isDeleted()).count()
                        : 0)
                .status(getProfileStatus(user))
                .addDate(user.getAddDate())
                .changeDate(user.getChangeDate())
                .deleted(user.isDeleted())
                .build();
    }

    /**
     * Convierte una entidad User a UserSummaryResponse
     */
    public UserSummaryResponse toUserSummaryResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserSummaryResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(getFullName(user))
                .rolName(user.getRol() != null ? user.getRol().getName() : "Sin rol")
                .totalAccounts(user.getAccounts() != null
                        ? (int) user.getAccounts().stream().filter(account -> !account.isDeleted()).count()
                        : 0)
                .status(getProfileStatus(user))
                .addDate(user.getAddDate())
                .deleted(user.isDeleted())
                .build();
    }

    /**
     * Convierte una entidad User a UserLoginResponse
     */
    public UserLoginResponse toUserLoginResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserLoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(getFullName(user))
                .rol(user.getRol() != null ? toRolSummary(user) : null)
                .status(getProfileStatus(user))
                .sessionToken(generateSessionToken(user))
                .lastAccess(user.getChangeDate())
                .firstLogin(isFirstLogin(user))
                .build();
    }

    /**
     * Convierte una lista de Users a UserSummaryResponse
     */
    public List<UserSummaryResponse> toUserSummaryResponseList(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toUserSummaryResponse)
                .collect(Collectors.toList());
    } // Métodos auxiliares privados

    private ProfileSummaryResponse toProfileSummary(User user) {
        if (user.getProfile() == null) {
            return null;
        }

        String fullName = getFullName(user);

        return ProfileSummaryResponse.builder()
                .id(user.getProfile().getId())
                .fullName(fullName)
                .ci(user.getProfile().getCi())
                .mobile(user.getProfile().getMobile())
                .status(user.getProfile().getStatus())
                .build();
    }

    private RolSummaryResponse toRolSummary(User user) {
        if (user.getRol() == null) {
            return null;
        }

        return RolSummaryResponse.builder()
                .id(user.getRol().getId())
                .name(user.getRol().getName())
                .description(user.getRol().getDescription())
                .active(true) // Asumimos que está activo si está asignado
                .build();
    }

    private AccountSummaryResponse toAccountSummary(com.uab.taller.store.domain.Account account) {
        return AccountSummaryResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .type(account.getType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .build();
    }

    private String getFullName(User user) {
        if (user.getProfile() == null) {
            return "Sin nombre";
        }

        String name = user.getProfile().getName() != null ? user.getProfile().getName() : "";
        String lastName = user.getProfile().getLastName() != null ? user.getProfile().getLastName() : "";

        return (name + " " + lastName).trim();
    }

    private String getProfileStatus(User user) {
        if (user.getProfile() != null && user.getProfile().getStatus() != null) {
            return user.getProfile().getStatus();
        }
        return user.isDeleted() ? "DELETED" : "ACTIVE";
    }

    private String generateSessionToken(User user) {
        // En un entorno real, esto debería ser un JWT o token seguro
        return "session_" + user.getId() + "_" + System.currentTimeMillis();
    }

    private boolean isFirstLogin(User user) {
        // Lógica para determinar si es el primer login
        // Por ejemplo, si changeDate es igual a addDate o similar
        return user.getChangeDate() == null ||
                user.getChangeDate().equals(user.getAddDate());
    }
}
