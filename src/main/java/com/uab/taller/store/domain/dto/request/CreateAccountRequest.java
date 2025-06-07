package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Size(max = 50, message = "El tipo de cuenta no puede exceder 50 caracteres")
    private String type;

    @NotBlank(message = "La moneda es obligatoria")
    @Size(max = 10, message = "La moneda no puede exceder 10 caracteres")
    private String currency;

    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial debe ser mayor o igual a 0")
    private BigDecimal initialBalance = BigDecimal.ZERO;

    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    private String status = "ACTIVE";
}
