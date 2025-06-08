package com.uab.taller.store.domain.dto.request;

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
public class CreateTransactionRequest {

    @NotNull(message = "El ID de la cuenta origen es obligatorio")
    private Long sourceAccountId;

    private Long targetAccountId;

    @NotBlank(message = "El tipo de transacción es obligatorio")
    @Pattern(regexp = "^(DEPOSIT|WITHDRAWAL|TRANSFER|PAYMENT)$", message = "El tipo de transacción debe ser DEPOSIT, WITHDRAWAL, TRANSFER o PAYMENT")
    private String transactionType;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Digits(integer = 15, fraction = 2, message = "El monto debe tener máximo 15 dígitos enteros y 2 decimales")
    private BigDecimal amount;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String description;

    @Size(max = 100, message = "La referencia no puede exceder 100 caracteres")
    private String reference;
}
