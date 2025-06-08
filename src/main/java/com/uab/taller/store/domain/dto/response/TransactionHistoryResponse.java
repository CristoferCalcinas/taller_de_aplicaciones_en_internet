package com.uab.taller.store.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "Historial completo de una transacción bancaria")
public class TransactionHistoryResponse {

    @Schema(description = "Información principal de la transacción")
    private TransactionInfo transaction;

    @Schema(description = "Lista de eventos relacionados con la transacción")
    private List<TransactionEvent> events;

    @Schema(description = "Información de auditoría")
    private AuditInfo auditInfo;

    @Data
    @Builder
    @Schema(description = "Información principal de la transacción")
    public static class TransactionInfo {
        @Schema(description = "ID único de la transacción", example = "12345")
        private Long id;

        @Schema(description = "Tipo de transacción", example = "TRANSFER")
        private String type;

        @Schema(description = "Monto de la transacción", example = "1500.00")
        private BigDecimal amount;

        @Schema(description = "Moneda", example = "BOB")
        private String currency;

        @Schema(description = "Estado actual", example = "COMPLETED")
        private String status;

        @Schema(description = "Fecha de creación")
        private LocalDateTime createdAt;

        @Schema(description = "Descripción de la transacción")
        private String description;

        @Schema(description = "Referencia externa")
        private String reference;
    }

    @Data
    @Builder
    @Schema(description = "Evento en el historial de la transacción")
    public static class TransactionEvent {
        @Schema(description = "Tipo de evento", example = "CREATED")
        private String eventType;

        @Schema(description = "Descripción del evento")
        private String description;

        @Schema(description = "Fecha del evento")
        private LocalDateTime timestamp;

        @Schema(description = "Usuario que ejecutó el evento")
        private String executedBy;

        @Schema(description = "Detalles adicionales del evento")
        private String details;
    }

    @Data
    @Builder
    @Schema(description = "Información de auditoría")
    public static class AuditInfo {
        @Schema(description = "Usuario que creó la transacción")
        private String createdBy;

        @Schema(description = "Fecha de creación")
        private LocalDateTime createdAt;

        @Schema(description = "Usuario que modificó por última vez")
        private String lastModifiedBy;

        @Schema(description = "Fecha de última modificación")
        private LocalDateTime lastModifiedAt;

        @Schema(description = "Número de modificaciones")
        private Integer modificationCount;
    }
}
