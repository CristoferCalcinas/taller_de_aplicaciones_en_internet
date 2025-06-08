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
@Schema(description = "DTO de respuesta para rol con información completa")
public class RolResponse {

    @Schema(description = "ID único del rol", example = "1")
    private Long id;

    @Schema(description = "Nombre del rol", example = "ADMIN")
    private String name;

    @Schema(description = "Descripción del rol", example = "Administrador del sistema con acceso completo")
    private String description;

    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    private LocalDateTime addDate;

    @Schema(description = "Usuario que creó el registro", example = "admin")
    private String addUser;

    @Schema(description = "Fecha de última modificación", example = "2024-01-20T14:45:00")
    private LocalDateTime changeDate;

    @Schema(description = "Usuario que realizó la última modificación", example = "admin")
    private String changeUser;

    @Schema(description = "Indica si el rol está eliminado", example = "false")
    private boolean deleted;

    @Schema(description = "Indica si el rol está activo", example = "true")
    public boolean isActive() {
        return !deleted;
    }

    /**
     * Método de fábrica estático para crear una instancia desde un Rol
     */
    public static RolResponse fromRol(com.uab.taller.store.domain.Rol rol) {
        if (rol == null) {
            return null;
        }

        return RolResponse.builder()
                .id(rol.getId())
                .name(rol.getName())
                .description(rol.getDescription())
                .addDate(rol.getAddDate())
                .addUser(rol.getAddUser())
                .changeDate(rol.getChangeDate())
                .changeUser(rol.getChangeUser())
                .deleted(rol.isDeleted())
                .build();
    }
}
