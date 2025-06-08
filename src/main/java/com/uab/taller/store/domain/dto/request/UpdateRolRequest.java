package com.uab.taller.store.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para actualizar un rol existente. El ID se pasa como path parameter, no en el body.")
public class UpdateRolRequest {

    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[A-Z][A-Z_]*$", message = "El nombre debe estar en mayúsculas y puede contener guiones bajos")
    @Schema(description = "Nombre del rol", example = "MODERATOR")
    private String name;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Schema(description = "Descripción del rol", example = "Moderador con permisos específicos")
    private String description;
}
