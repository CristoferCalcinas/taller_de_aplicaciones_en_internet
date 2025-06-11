package com.uab.taller.store.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para operaciones de autenticación")
public class UserLoginResponse {

    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Correo electrónico del usuario", example = "usuario@ejemplo.com")
    private String email;

    @Schema(description = "Nombre completo del usuario", example = "Juan Carlos Pérez")
    private String fullName;

    @Schema(description = "Información del rol del usuario")
    private RolSummaryResponse rol;

    @Schema(description = "Estado del usuario", example = "ACTIVE")
    private String status;

    @Schema(description = "Token de sesión (temporal)")
    private String sessionToken;

    @Schema(description = "Último acceso", example = "2024-02-20T15:45:00")
    private LocalDateTime lastAccess;

    @Schema(description = "Indica si es el primer login", example = "false")
    private boolean firstLogin;
}
