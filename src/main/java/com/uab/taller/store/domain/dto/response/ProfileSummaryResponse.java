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
@Schema(description = "DTO de respuesta simplificado para perfil")
public class ProfileSummaryResponse {

    @Schema(description = "ID único del perfil", example = "1")
    private Long id;

    @Schema(description = "Nombre completo", example = "Juan Carlos Pérez García")
    private String fullName;

    @Schema(description = "Cédula de identidad", example = "12345678-9")
    private String ci;

    @Schema(description = "Estado del perfil", example = "ACTIVE")
    private String status;

    @Schema(description = "Número de teléfono móvil", example = "+591 77777777")
    private String mobile;

    @Schema(description = "Indica si el perfil está activo", example = "true")
    private boolean active;
}
