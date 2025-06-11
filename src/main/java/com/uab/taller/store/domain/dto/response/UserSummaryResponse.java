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
@Schema(description = "DTO de respuesta resumida para usuario")
public class UserSummaryResponse {

    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Correo electrónico del usuario", example = "usuario@ejemplo.com")
    private String email;

    @Schema(description = "Nombre completo del usuario", example = "Juan Carlos Pérez")
    private String fullName;

    @Schema(description = "Nombre del rol", example = "CLIENTE")
    private String rolName;

    @Schema(description = "Cantidad de cuentas del usuario", example = "2")
    private Integer totalAccounts;

    @Schema(description = "Estado del usuario", example = "ACTIVE")
    private String status;

    @Schema(description = "Fecha de registro", example = "2024-01-15T10:30:00")
    private LocalDateTime addDate;

    @Schema(description = "Indica si el usuario está eliminado", example = "false")
    private boolean deleted;
}
