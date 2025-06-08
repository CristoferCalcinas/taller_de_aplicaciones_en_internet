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
@Schema(description = "DTO de respuesta para perfil con información completa")
public class ProfileResponse {

    @Schema(description = "ID único del perfil", example = "1")
    private Long id;

    @Schema(description = "Nombre del perfil", example = "Juan Carlos")
    private String name;

    @Schema(description = "Apellido del perfil", example = "Pérez García")
    private String lastName;

    @Schema(description = "Cédula de identidad", example = "12345678-9")
    private String ci;

    @Schema(description = "Número de teléfono móvil", example = "+591 77777777")
    private String mobile;

    @Schema(description = "Dirección de residencia", example = "Av. Heroínas 123, Cochabamba")
    private String address;

    @Schema(description = "Estado del perfil", example = "ACTIVE")
    private String status;

    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    private LocalDateTime addDate;

    @Schema(description = "Usuario que creó el registro", example = "admin")
    private String addUser;

    @Schema(description = "Fecha de última modificación", example = "2024-01-20T14:45:00")
    private LocalDateTime changeDate;

    @Schema(description = "Usuario que realizó la última modificación", example = "admin")
    private String changeUser;

    @Schema(description = "Indica si el perfil está eliminado", example = "false")
    private boolean deleted;

    @Schema(description = "Nombre completo formateado", example = "Juan Carlos Pérez García")
    public String getFullName() {
        return (name != null ? name : "") + " " + (lastName != null ? lastName : "");
    }

    @Schema(description = "Indica si el perfil está activo", example = "true")
    public boolean isActive() {
        return "ACTIVE".equals(status) && !deleted;
    }
}
