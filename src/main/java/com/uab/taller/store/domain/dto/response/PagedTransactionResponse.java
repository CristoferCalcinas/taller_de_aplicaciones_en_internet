package com.uab.taller.store.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Respuesta paginada de transacciones")
public class PagedTransactionResponse {

    @Schema(description = "Lista de transacciones en la página actual")
    private List<TransactionSummaryResponse> transactions;

    @Schema(description = "Información de paginación")
    private PageInfo pageInfo;

    @Schema(description = "Información de filtros aplicados")
    private FilterInfo filterInfo;

    @Data
    @Builder
    @Schema(description = "Información de paginación")
    public static class PageInfo {
        @Schema(description = "Página actual", example = "0")
        private Integer currentPage;

        @Schema(description = "Tamaño de página", example = "20")
        private Integer pageSize;

        @Schema(description = "Número total de elementos", example = "150")
        private Long totalElements;

        @Schema(description = "Número total de páginas", example = "8")
        private Integer totalPages;

        @Schema(description = "Es la primera página", example = "true")
        private Boolean isFirst;

        @Schema(description = "Es la última página", example = "false")
        private Boolean isLast;

        @Schema(description = "Tiene página anterior", example = "false")
        private Boolean hasPrevious;

        @Schema(description = "Tiene página siguiente", example = "true")
        private Boolean hasNext;
    }

    @Data
    @Builder
    @Schema(description = "Información de filtros aplicados")
    public static class FilterInfo {
        @Schema(description = "Número de filtros aplicados", example = "3")
        private Integer appliedFilters;

        @Schema(description = "Criterios de búsqueda aplicados")
        private String searchCriteria;

        @Schema(description = "Ordenamiento aplicado", example = "date desc")
        private String sorting;
    }
}
