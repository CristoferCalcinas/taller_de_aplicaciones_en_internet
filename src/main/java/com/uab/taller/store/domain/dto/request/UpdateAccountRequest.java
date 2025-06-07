package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {

    @NotNull(message = "El ID de la cuenta es obligatorio")
    private Long id;

    @Size(max = 50, message = "El tipo de cuenta no puede exceder 50 caracteres")
    private String type;

    @Size(max = 10, message = "La moneda no puede exceder 10 caracteres")
    private String currency;

    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo debe ser mayor o igual a 0")
    private BigDecimal balance;

    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    private String status;
}
