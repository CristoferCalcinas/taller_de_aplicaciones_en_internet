package com.uab.taller.store.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "DTO para actualizar un usuario existente")
public class UpdateUserRequest {

    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Schema(description = "Nuevo correo electrónico del usuario", example = "nuevo@ejemplo.com")
    private String email;

    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "La contraseña debe contener al menos una letra minúscula, una mayúscula y un número")
    @Schema(description = "Nueva contraseña del usuario (opcional)", example = "NuevaPass123")
    private String password;

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    @Schema(description = "Nuevo nombre del usuario", example = "Juan Carlos")
    private String name;

    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    @Schema(description = "Nuevo apellido del usuario", example = "Pérez García")
    private String lastName;

    @Size(min = 5, max = 20, message = "El CI debe tener entre 5 y 20 caracteres")
    @Pattern(regexp = "^[0-9A-Za-z-]+$", message = "El CI solo puede contener números, letras y guiones")
    @Schema(description = "Nueva cédula de identidad", example = "12345678-9")
    private String ci;

    @Pattern(regexp = "^[+]?[0-9\\s-()]+$", message = "El formato del teléfono no es válido")
    @Size(max = 20, message = "El móvil no puede exceder 20 caracteres")
    @Schema(description = "Nuevo número de teléfono móvil", example = "+591 77777777")
    private String mobile;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    @Schema(description = "Nueva dirección del usuario", example = "Av. Ejemplo 123, La Paz")
    private String address;

    @Pattern(regexp = "^(ACTIVE|INACTIVE|SUSPENDED|PENDING)$", message = "El estado debe ser: ACTIVE, INACTIVE, SUSPENDED o PENDING")
    @Schema(description = "Nuevo estado del usuario", example = "ACTIVE", allowableValues = { "ACTIVE", "INACTIVE",
            "SUSPENDED", "PENDING" })
    private String status;
}
