package com.uab.taller.store.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumen de transacción con información básica")
public class TransactionSummaryResponse {
    @Schema(description = "ID único de la transacción", example = "1001")
    private Long id;

    @Schema(description = "Tipo de transacción", example = "TRANSFER")
    private String transactionType;

    @Schema(description = "Monto de la transacción", example = "150.75")
    private BigDecimal amount;

    @Schema(description = "Fecha y hora de la transacción", example = "2024-06-08T10:30:00")
    private LocalDateTime date;

    @Schema(description = "Número de cuenta origen", example = "ACC-001-123456")
    private String sourceAccountNumber;

    @Schema(description = "Número de cuenta destino", example = "ACC-002-789012")
    private String targetAccountNumber;

    @Schema(description = "Descripción de la transacción", example = "Pago servicios públicos")
    private String description;

    @Schema(description = "Estado de la transacción", example = "COMPLETED")
    private String status;

    @Schema(description = "Moneda de la transacción", example = "USD")
    private String currency;
}
