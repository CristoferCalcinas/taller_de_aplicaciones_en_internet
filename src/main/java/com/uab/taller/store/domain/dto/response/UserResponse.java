package com.uab.taller.store.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para usuario con información completa")
public class UserResponse {

    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Correo electrónico del usuario", example = "usuario@ejemplo.com")
    private String email;

    @Schema(description = "Información del perfil del usuario")
    private ProfileSummaryResponse profile;

    @Schema(description = "Información del rol del usuario")
    private RolSummaryResponse rol;

    @Schema(description = "Lista de cuentas del usuario")
    private List<AccountSummaryResponse> accounts;

    @Schema(description = "Cantidad total de cuentas", example = "2")
    private Integer totalAccounts;

    @Schema(description = "Estado del usuario", example = "ACTIVE")
    private String status;

    @Schema(description = "Fecha de registro", example = "2024-01-15T10:30:00")
    private LocalDateTime addDate;

    @Schema(description = "Fecha de última modificación", example = "2024-02-20T15:45:00")
    private LocalDateTime changeDate;

    @Schema(description = "Indica si el usuario está eliminado", example = "false")
    private boolean deleted;
}
