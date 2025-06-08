package com.uab.taller.store.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta simplificado para rol")
public class RolSummaryResponse {

    @Schema(description = "ID único del rol", example = "1")
    private Long id;

    @Schema(description = "Nombre del rol", example = "ADMIN")
    private String name;

    @Schema(description = "Descripción del rol", example = "Administrador del sistema")
    private String description;

    @Schema(description = "Indica si el rol está activo", example = "true")
    private boolean active;

    /**
     * Método de fábrica estático para crear una instancia desde un Rol
     */
    public static RolSummaryResponse fromRol(com.uab.taller.store.domain.Rol rol) {
        if (rol == null) {
            return null;
        }

        return RolSummaryResponse.builder()
                .id(rol.getId())
                .name(rol.getName())
                .description(rol.getDescription())
                .active(!rol.isDeleted())
                .build();
    }
}
