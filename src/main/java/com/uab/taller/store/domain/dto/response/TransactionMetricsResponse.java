package com.uab.taller.store.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO de respuesta para métricas y dashboard ejecutivo del sistema
 */
@Data
@Builder
@Schema(description = "Métricas y resumen ejecutivo del sistema de transacciones")
public class TransactionMetricsResponse {

    @Schema(description = "Resumen general del sistema")
    private SystemSummary systemSummary;

    @Schema(description = "Métricas por tipo de transacción")
    private List<TransactionTypeMetrics> transactionTypeMetrics;

    @Schema(description = "Tendencias temporales")
    private TemporalTrends temporalTrends;

    @Schema(description = "Métricas de rendimiento")
    private PerformanceMetrics performanceMetrics;

    @Schema(description = "Alertas y notificaciones")
    private List<SystemAlert> alerts;

    @Data
    @Builder
    @Schema(description = "Resumen general del sistema")
    public static class SystemSummary {
        @Schema(description = "Total de transacciones en el sistema", example = "15420")
        private Long totalTransactions;

        @Schema(description = "Volumen total de dinero procesado", example = "2500000.00")
        private BigDecimal totalVolume;

        @Schema(description = "Número de cuentas activas", example = "1250")
        private Long activeAccounts;

        @Schema(description = "Transacciones del día", example = "342")
        private Long dailyTransactions;

        @Schema(description = "Volumen del día", example = "45600.00")
        private BigDecimal dailyVolume;

        @Schema(description = "Tiempo de generación del reporte")
        private LocalDateTime generatedAt;

        @Schema(description = "Porcentaje de transacciones exitosas", example = "98.5")
        private Double successRate;
    }

    @Data
    @Builder
    @Schema(description = "Métricas por tipo de transacción")
    public static class TransactionTypeMetrics {
        @Schema(description = "Tipo de transacción", example = "TRANSFER")
        private String transactionType;

        @Schema(description = "Número de transacciones", example = "8540")
        private Long count;

        @Schema(description = "Volumen total", example = "1200000.00")
        private BigDecimal totalAmount;

        @Schema(description = "Monto promedio", example = "140.50")
        private BigDecimal averageAmount;

        @Schema(description = "Porcentaje del total", example = "55.4")
        private Double percentage;

        @Schema(description = "Tasa de crecimiento mensual", example = "12.3")
        private Double monthlyGrowthRate;
    }

    @Data
    @Builder
    @Schema(description = "Tendencias temporales")
    public static class TemporalTrends {
        @Schema(description = "Transacciones por hora del día")
        private Map<Integer, Long> hourlyDistribution;

        @Schema(description = "Transacciones por día de la semana")
        private Map<String, Long> weeklyDistribution;

        @Schema(description = "Volumen por mes")
        private Map<String, BigDecimal> monthlyVolume;

        @Schema(description = "Pico de actividad del día", example = "14")
        private Integer peakHour;

        @Schema(description = "Día más activo de la semana", example = "MIÉRCOLES")
        private String peakDay;
    }

    @Data
    @Builder
    @Schema(description = "Métricas de rendimiento del sistema")
    public static class PerformanceMetrics {
        @Schema(description = "Tiempo promedio de procesamiento (ms)", example = "125.5")
        private Double averageProcessingTime;

        @Schema(description = "Transacciones procesadas por segundo", example = "45.2")
        private Double transactionsPerSecond;

        @Schema(description = "Tiempo de respuesta del 95 percentil (ms)", example = "250.0")
        private Double p95ResponseTime;

        @Schema(description = "Disponibilidad del sistema (%)", example = "99.8")
        private Double systemUptime;

        @Schema(description = "Transacciones fallidas en las últimas 24h", example = "12")
        private Long failedTransactions24h;
    }

    @Data
    @Builder
    @Schema(description = "Alerta del sistema")
    public static class SystemAlert {
        @Schema(description = "Tipo de alerta", example = "WARNING")
        private String alertType;

        @Schema(description = "Mensaje de la alerta")
        private String message;

        @Schema(description = "Nivel de severidad", example = "MEDIUM")
        private String severity;

        @Schema(description = "Fecha de la alerta")
        private LocalDateTime timestamp;

        @Schema(description = "¿Requiere acción?", example = "false")
        private Boolean requiresAction;
    }
}
