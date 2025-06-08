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
@Schema(description = "DTO para actualizar un perfil existente. El ID se pasa como path parameter, no en el body.")
public class UpdateProfileRequest {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    @Schema(description = "Nombre del perfil", example = "Juan Carlos")
    private String name;

    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    @Schema(description = "Apellido del perfil", example = "Pérez García")
    private String lastName;

    @Size(min = 5, max = 20, message = "El CI debe tener entre 5 y 20 caracteres")
    @Pattern(regexp = "^[0-9A-Za-z-]+$", message = "El CI solo puede contener números, letras y guiones")
    @Schema(description = "Cédula de identidad", example = "12345678-9")
    private String ci;

    @Size(max = 20, message = "El número de móvil no puede exceder 20 caracteres")
    @Pattern(regexp = "^[+]?[0-9\\s()-]+$", message = "Formato de móvil inválido")
    @Schema(description = "Número de teléfono móvil", example = "+591 77777777")
    private String mobile;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    @Schema(description = "Dirección de residencia", example = "Av. Heroínas 123, Cochabamba")
    private String address;

    @Pattern(regexp = "^(ACTIVE|INACTIVE|PENDING)$", message = "El estado debe ser ACTIVE, INACTIVE o PENDING")
    @Schema(description = "Estado del perfil", example = "ACTIVE", allowableValues = { "ACTIVE", "INACTIVE",
            "PENDING" })
    private String status;
}
