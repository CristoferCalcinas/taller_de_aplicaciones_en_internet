package com.uab.taller.store.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Filtros para búsqueda avanzada de transacciones")
public class TransactionSearchRequest {

    @Schema(description = "ID de cuenta origen", example = "12345")
    private Long sourceAccountId;

    @Schema(description = "ID de cuenta destino", example = "67890")
    private Long targetAccountId;

    @Schema(description = "Tipo de transacción", example = "TRANSFER", allowableValues = { "DEPOSIT", "WITHDRAWAL",
            "TRANSFER", "PAYMENT" })
    private String transactionType;

    @Schema(description = "Monto mínimo", example = "100.00")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto mínimo debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal minAmount;

    @Schema(description = "Monto máximo", example = "5000.00")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto máximo debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal maxAmount;

    @Schema(description = "Fecha de inicio", example = "2024-01-01T00:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @Schema(description = "Fecha de fin", example = "2024-12-31T23:59:59")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    @Schema(description = "Estado de la transacción", example = "COMPLETED")
    private String status;

    @Schema(description = "Moneda", example = "BOB")
    private String currency;

    @Schema(description = "Texto a buscar en descripción o referencia", example = "pago servicios")
    private String searchText;

    @Schema(description = "Página", example = "0", minimum = "0")
    private Integer page = 0;

    @Schema(description = "Tamaño de página", example = "20", minimum = "1", maximum = "100")
    private Integer size = 20;

    @Schema(description = "Campo de ordenamiento", example = "date", allowableValues = { "date", "amount",
            "transactionType" })
    private String sortBy = "date";

    @Schema(description = "Dirección de ordenamiento", example = "desc", allowableValues = { "asc", "desc" })
    private String sortDirection = "desc";
}
