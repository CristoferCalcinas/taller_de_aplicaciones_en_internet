package com.uab.taller.store.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear un nuevo usuario")
public class CreateUserRequest {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Schema(description = "Correo electrónico del usuario", example = "usuario@ejemplo.com", required = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "La contraseña debe contener al menos una letra minúscula, una mayúscula y un número")
    @Schema(description = "Contraseña del usuario", example = "MiPassword123", required = true)
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    @Schema(description = "Nombre del usuario", example = "Juan Carlos", required = true)
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    @Schema(description = "Apellido del usuario", example = "Pérez García", required = true)
    private String lastName;

    @Size(min = 5, max = 20, message = "El CI debe tener entre 5 y 20 caracteres")
    @Pattern(regexp = "^[0-9A-Za-z-]+$", message = "El CI solo puede contener números, letras y guiones")
    @Schema(description = "Cédula de identidad (opcional)", example = "12345678-9")
    private String ci;

    @Pattern(regexp = "^[+]?[0-9\\s-()]+$", message = "El formato del teléfono no es válido")
    @Size(max = 20, message = "El móvil no puede exceder 20 caracteres")
    @Schema(description = "Número de teléfono móvil (opcional)", example = "+591 77777777")
    private String mobile;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    @Schema(description = "Dirección del usuario (opcional)", example = "Av. Ejemplo 123, La Paz")
    private String address;

    // Campos opcionales para crear cuenta automáticamente
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El saldo debe tener máximo 13 dígitos enteros y 2 decimales")
    @Schema(description = "Saldo inicial para crear cuenta automáticamente (opcional)", example = "1000.00")
    private BigDecimal saldoInicial;

    @Pattern(regexp = "^(SAVINGS|CHECKING|CREDIT)$", message = "El tipo de cuenta debe ser: SAVINGS, CHECKING o CREDIT")
    @Schema(description = "Tipo de cuenta a crear automáticamente (opcional)", example = "SAVINGS", allowableValues = {
            "SAVINGS", "CHECKING", "CREDIT" })
    private String tipoCuenta;

    @Pattern(regexp = "^(BOB|USD|EUR)$", message = "La moneda debe ser: BOB, USD o EUR")
    @Schema(description = "Moneda de la cuenta (opcional, por defecto BOB)", example = "BOB", allowableValues = { "BOB",
            "USD", "EUR" })
    private String moneda = "BOB";
}
