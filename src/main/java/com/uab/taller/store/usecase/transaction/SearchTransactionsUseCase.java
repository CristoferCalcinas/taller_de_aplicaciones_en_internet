package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.request.TransactionSearchRequest;
import com.uab.taller.store.domain.dto.response.PagedTransactionResponse;
import com.uab.taller.store.domain.dto.response.TransactionSummaryResponse;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso para búsqueda avanzada de transacciones
 */
@Service
public class SearchTransactionsUseCase {

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private TransactionMappingUseCase mappingUseCase;

    /**
     * Busca transacciones aplicando filtros avanzados
     */
    public PagedTransactionResponse searchTransactions(TransactionSearchRequest searchRequest) {
        // Obtener todas las transacciones (en producción, esto debería usar un
        // repositorio con paginación)
        List<Transaction> allTransactions = transactionService.findAll();

        // Aplicar filtros
        List<Transaction> filteredTransactions = allTransactions.stream()
                .filter(transaction -> matchesFilters(transaction, searchRequest))
                .collect(Collectors.toList());

        // Ordenar
        filteredTransactions = sortTransactions(filteredTransactions, searchRequest);

        // Paginar manualmente (en producción, esto debería hacerse en la base de datos)
        int startIndex = searchRequest.getPage() * searchRequest.getSize();
        int endIndex = Math.min(startIndex + searchRequest.getSize(), filteredTransactions.size());

        List<Transaction> pagedTransactions = filteredTransactions.subList(
                Math.min(startIndex, filteredTransactions.size()),
                endIndex);

        // Convertir a DTOs
        List<TransactionSummaryResponse> transactionResponses = pagedTransactions.stream()
                .map(mappingUseCase::toSummaryResponse)
                .collect(Collectors.toList());

        // Construir información de paginación
        PagedTransactionResponse.PageInfo pageInfo = buildPageInfo(
                searchRequest, filteredTransactions.size());

        // Construir información de filtros
        PagedTransactionResponse.FilterInfo filterInfo = buildFilterInfo(searchRequest);

        return PagedTransactionResponse.builder()
                .transactions(transactionResponses)
                .pageInfo(pageInfo)
                .filterInfo(filterInfo)
                .build();
    }

    /**
     * Verifica si una transacción coincide con los filtros
     */
    private boolean matchesFilters(Transaction transaction, TransactionSearchRequest request) {
        // Filtro por cuenta origen
        if (request.getSourceAccountId() != null &&
                (transaction.getSourceAccount() == null ||
                        !transaction.getSourceAccount().getId().equals(request.getSourceAccountId()))) {
            return false;
        }

        // Filtro por cuenta destino
        if (request.getTargetAccountId() != null &&
                (transaction.getTargetAccount() == null ||
                        !transaction.getTargetAccount().getId().equals(request.getTargetAccountId()))) {
            return false;
        }

        // Filtro por tipo de transacción
        if (request.getTransactionType() != null &&
                !request.getTransactionType().equalsIgnoreCase(transaction.getTransactionType())) {
            return false;
        }

        // Filtro por monto mínimo
        if (request.getMinAmount() != null &&
                transaction.getAmount().compareTo(request.getMinAmount()) < 0) {
            return false;
        }

        // Filtro por monto máximo
        if (request.getMaxAmount() != null &&
                transaction.getAmount().compareTo(request.getMaxAmount()) > 0) {
            return false;
        }

        // Filtro por fecha de inicio
        if (request.getStartDate() != null &&
                transaction.getDate().isBefore(request.getStartDate())) {
            return false;
        }

        // Filtro por fecha de fin
        if (request.getEndDate() != null &&
                transaction.getDate().isAfter(request.getEndDate())) {
            return false;
        }

        // Filtro por estado
        if (request.getStatus() != null &&
                !request.getStatus().equalsIgnoreCase(transaction.getStatus())) {
            return false;
        }

        // Filtro por moneda
        if (request.getCurrency() != null &&
                !request.getCurrency().equalsIgnoreCase(transaction.getCurrency())) {
            return false;
        }

        // Filtro por texto de búsqueda
        if (request.getSearchText() != null && !request.getSearchText().trim().isEmpty()) {
            String searchLower = request.getSearchText().toLowerCase();
            boolean foundInDescription = transaction.getDescription() != null &&
                    transaction.getDescription().toLowerCase().contains(searchLower);
            boolean foundInReference = transaction.getReference() != null &&
                    transaction.getReference().toLowerCase().contains(searchLower);

            if (!foundInDescription && !foundInReference) {
                return false;
            }
        }

        return true;
    }

    /**
     * Ordena las transacciones según los criterios especificados
     */
    private List<Transaction> sortTransactions(List<Transaction> transactions,
            TransactionSearchRequest request) {
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "date";
        String direction = request.getSortDirection() != null ? request.getSortDirection() : "desc";
        boolean ascending = "asc".equalsIgnoreCase(direction);

        return transactions.stream()
                .sorted((t1, t2) -> {
                    int comparison = 0;
                    switch (sortBy.toLowerCase()) {
                        case "amount":
                            comparison = t1.getAmount().compareTo(t2.getAmount());
                            break;
                        case "transactiontype":
                            comparison = t1.getTransactionType().compareTo(t2.getTransactionType());
                            break;
                        case "date":
                        default:
                            comparison = t1.getDate().compareTo(t2.getDate());
                            break;
                    }
                    return ascending ? comparison : -comparison;
                })
                .collect(Collectors.toList());
    }

    /**
     * Construye la información de paginación
     */
    private PagedTransactionResponse.PageInfo buildPageInfo(TransactionSearchRequest request,
            int totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / request.getSize());

        return PagedTransactionResponse.PageInfo.builder()
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .totalElements((long) totalElements)
                .totalPages(totalPages)
                .isFirst(request.getPage() == 0)
                .isLast(request.getPage() >= totalPages - 1)
                .hasPrevious(request.getPage() > 0)
                .hasNext(request.getPage() < totalPages - 1)
                .build();
    }

    /**
     * Construye la información de filtros aplicados
     */
    private PagedTransactionResponse.FilterInfo buildFilterInfo(TransactionSearchRequest request) {
        int appliedFilters = 0;
        StringBuilder criteria = new StringBuilder();

        if (request.getSourceAccountId() != null) {
            appliedFilters++;
            criteria.append("Cuenta origen: ").append(request.getSourceAccountId()).append("; ");
        }
        if (request.getTargetAccountId() != null) {
            appliedFilters++;
            criteria.append("Cuenta destino: ").append(request.getTargetAccountId()).append("; ");
        }
        if (request.getTransactionType() != null) {
            appliedFilters++;
            criteria.append("Tipo: ").append(request.getTransactionType()).append("; ");
        }
        if (request.getMinAmount() != null || request.getMaxAmount() != null) {
            appliedFilters++;
            criteria.append("Rango monto: ")
                    .append(request.getMinAmount() != null ? request.getMinAmount() : "N/A")
                    .append(" - ")
                    .append(request.getMaxAmount() != null ? request.getMaxAmount() : "N/A")
                    .append("; ");
        }

        String sorting = String.format("%s %s",
                request.getSortBy() != null ? request.getSortBy() : "date",
                request.getSortDirection() != null ? request.getSortDirection() : "desc");

        return PagedTransactionResponse.FilterInfo.builder()
                .appliedFilters(appliedFilters)
                .searchCriteria(criteria.toString())
                .sorting(sorting)
                .build();
    }
}
