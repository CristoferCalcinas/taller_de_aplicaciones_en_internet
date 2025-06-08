package com.uab.taller.store.controller;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.request.*;
import com.uab.taller.store.domain.dto.response.*;
import com.uab.taller.store.usecase.transaction.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/transaction")
@Validated
@Tag(name = "Transaction", description = "API para gestión completa de transacciones bancarias")
public class TransactionController {

    @Autowired
    CreateTransactionUseCase createTransactionUseCase;

    @Autowired
    CreateTransactionWithDtoUseCase createTransactionWithDtoUseCase;

    @Autowired
    GetAllTransactionsUseCase getAllTransactionsUseCase;

    @Autowired
    GetTransactionsWithDtoUseCase getTransactionsWithDtoUseCase;

    @Autowired
    GetTransactionByIdUseCase getTransactionByIdUseCase;

    @Autowired
    GetTransactionsByAccountUseCase getTransactionsByAccountUseCase;

    @Autowired
    DeleteTransactionUseCase deleteTransactionUseCase;

    @Autowired
    UpdateTransactionUseCase updateTransactionUseCase;

    @Autowired
    ProcessTransferUseCase processTransferUseCase;

    @Autowired
    ProcessDepositUseCase processDepositUseCase;

    @Autowired
    ProcessWithdrawalUseCase processWithdrawalUseCase;

    @Autowired
    GetTransactionsByDateRangeUseCase getTransactionsByDateRangeUseCase;

    @Autowired
    GetTransactionsByTypeUseCase getTransactionsByTypeUseCase;

    @Autowired
    TransactionStatisticsUseCase transactionStatisticsUseCase;

    @Autowired
    GetTransactionHistoryUseCase getTransactionHistoryUseCase;
    @Autowired
    SearchTransactionsUseCase searchTransactionsUseCase;

    @Autowired
    TransactionMetricsUseCase transactionMetricsUseCase;

    // ===============================
    // ENDPOINTS PARA CREAR TRANSACCIONES
    // ===============================

    @Operation(summary = "Crear nueva transacción", description = "Crea una nueva transacción bancaria con validaciones completas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción creada exitosamente", content = @Content(schema = @Schema(implementation = TransactionDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<TransactionDetailResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {
        TransactionDetailResponse response = createTransactionWithDtoUseCase.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear transacción (legacy)", description = "Endpoint legacy para crear transacciones. Usar el endpoint principal en su lugar.")
    @PostMapping("/legacy")
    public ResponseEntity<Transaction> createTransactionLegacy(@RequestBody Transaction transaction) {
        Transaction created = createTransactionUseCase.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ===============================
    // ENDPOINTS PARA CONSULTAR TRANSACCIONES
    // ===============================

    @Operation(summary = "Obtener todas las transacciones", description = "Recupera una lista resumida de todas las transacciones del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones obtenida exitosamente", content = @Content(schema = @Schema(implementation = TransactionSummaryResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<TransactionSummaryResponse>> getAllTransactions() {
        List<TransactionSummaryResponse> transactions = getTransactionsWithDtoUseCase.getAllTransactionsSummary();
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Obtener transacción por ID", description = "Recupera los detalles completos de una transacción específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción encontrada", content = @Content(schema = @Schema(implementation = TransactionDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada"),
            @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDetailResponse> getTransactionById(
            @Parameter(description = "ID único de la transacción", required = true) @PathVariable @NotNull @Positive Long id) {
        TransactionDetailResponse transaction = getTransactionsWithDtoUseCase.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Obtener transacciones por cuenta", description = "Recupera todas las transacciones asociadas a una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones de la cuenta", content = @Content(schema = @Schema(implementation = TransactionSummaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
            @ApiResponse(responseCode = "400", description = "ID de cuenta inválido")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionSummaryResponse>> getTransactionsByAccount(
            @Parameter(description = "ID único de la cuenta", required = true) @PathVariable @NotNull @Positive Long accountId) {
        List<TransactionSummaryResponse> transactions = getTransactionsWithDtoUseCase
                .getTransactionsByAccount(accountId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Obtener transacciones por rango de fechas", description = "Recupera transacciones filtradas por un período específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones en el rango especificado"),
            @ApiResponse(responseCode = "400", description = "Formato de fecha inválido")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
            @Parameter(description = "Fecha de inicio (formato ISO: yyyy-MM-ddTHH:mm:ss)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Fecha de fin (formato ISO: yyyy-MM-ddTHH:mm:ss)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Transaction> transactions = getTransactionsByDateRangeUseCase.getTransactionsByDateRange(startDate,
                endDate);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Obtener transacciones por tipo", description = "Recupera transacciones filtradas por tipo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones del tipo especificado"),
            @ApiResponse(responseCode = "400", description = "Tipo de transacción inválido")
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Transaction>> getTransactionsByType(
            @Parameter(description = "Tipo de transacción (DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT)", required = true) @PathVariable String type) {
        List<Transaction> transactions = getTransactionsByTypeUseCase.getTransactionsByType(type);
        return ResponseEntity.ok(transactions);
    }

    // ===============================
    // ENDPOINTS PARA OPERACIONES FINANCIERAS
    // ===============================

    @Operation(summary = "Procesar transferencia", description = "Realiza una transferencia de fondos entre dos cuentas con validaciones completas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferencia procesada exitosamente", content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Datos de transferencia inválidos"),
            @ApiResponse(responseCode = "404", description = "Una o ambas cuentas no encontradas"),
            @ApiResponse(responseCode = "422", description = "Saldo insuficiente o cuenta inactiva")
    })
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> processTransfer(
            @Valid @RequestBody TransferRequest transferRequest) {
        Transaction transaction = processTransferUseCase.processTransfer(transferRequest);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Procesar depósito", description = "Realiza un depósito de fondos en una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Depósito procesado exitosamente", content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Datos de depósito inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
            @ApiResponse(responseCode = "422", description = "Cuenta inactiva")
    })
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> processDeposit(
            @Valid @RequestBody DepositRequest depositRequest) {
        Transaction transaction = processDepositUseCase.processDeposit(depositRequest);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Procesar retiro", description = "Realiza un retiro de fondos de una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retiro procesado exitosamente", content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Datos de retiro inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
            @ApiResponse(responseCode = "422", description = "Saldo insuficiente o cuenta inactiva")
    })
    @PostMapping("/withdrawal")
    public ResponseEntity<Transaction> processWithdrawal(
            @Valid @RequestBody WithdrawalRequest withdrawalRequest) {
        Transaction transaction = processWithdrawalUseCase.processWithdrawal(withdrawalRequest);
        return ResponseEntity.ok(transaction);
    } // ===============================
      // ENDPOINTS PARA GESTIÓN DE TRANSACCIONES
      // ===============================

    @Operation(summary = "Actualizar transacción", description = "Actualiza información limitada de una transacción existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción actualizada exitosamente", content = @Content(schema = @Schema(implementation = TransactionDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada"),
            @ApiResponse(responseCode = "422", description = "Transacción no puede ser modificada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDetailResponse> updateTransaction(
            @Parameter(description = "ID único de la transacción a actualizar", required = true) @PathVariable @NotNull @Positive Long id,
            @Valid @RequestBody UpdateTransactionRequest request) {
        // Implementar lógica de actualización usando DTOs
        Transaction existingTransaction = updateTransactionUseCase.update(
                updateTransactionUseCase.buildTransactionFromUpdateRequest(id, request));
        TransactionDetailResponse response = getTransactionsWithDtoUseCase
                .getTransactionById(existingTransaction.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar transacción", description = "Elimina una transacción específica (sujeto a reglas de negocio)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada"),
            @ApiResponse(responseCode = "422", description = "Transacción no puede ser eliminada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteTransactionById(
            @Parameter(description = "ID único de la transacción a eliminar", required = true) @PathVariable @NotNull @Positive Long id) {
        DeleteResponse response = deleteTransactionUseCase.deleteById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Reversar transacción", description = "Crea una transacción reversa para anular los efectos de una transacción original")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción reversada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada"),
            @ApiResponse(responseCode = "422", description = "Transacción no puede ser reversada")
    })
    @PostMapping("/{id}/reverse")
    public ResponseEntity<DeleteResponse> reverseTransaction(
            @Parameter(description = "ID único de la transacción a reversar", required = true) @PathVariable @NotNull @Positive Long id) {
        DeleteResponse response = deleteTransactionUseCase.reverseTransaction(id);
        return ResponseEntity.ok(response);
    }

    // ===============================
    // ENDPOINTS PARA ESTADÍSTICAS Y REPORTES
    // ===============================

    @Operation(summary = "Obtener estadísticas por cuenta", description = "Genera un reporte estadístico de las transacciones de una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas generadas exitosamente", content = @Content(schema = @Schema(implementation = TransactionStatisticsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/statistics/account/{accountId}")
    public ResponseEntity<TransactionStatisticsResponse> getStatisticsByAccount(
            @Parameter(description = "ID único de la cuenta", required = true) @PathVariable @NotNull @Positive Long accountId) {
        TransactionStatisticsResponse statistics = transactionStatisticsUseCase.getStatisticsByAccount(accountId);
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "Obtener estadísticas por rango de fechas", description = "Genera un reporte estadístico de las transacciones en un período específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas generadas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Formato de fecha inválido")
    })
    @GetMapping("/statistics/date-range")
    public ResponseEntity<TransactionStatisticsResponse> getStatisticsByDateRange(
            @Parameter(description = "Fecha de inicio", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Fecha de fin", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        TransactionStatisticsResponse statistics = transactionStatisticsUseCase.getStatisticsByDateRange(startDate,
                endDate);
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "Obtener estadísticas generales", description = "Genera un reporte estadístico general de todas las transacciones del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas generales obtenidas exitosamente")
    })
    @GetMapping("/statistics/general")
    public ResponseEntity<TransactionStatisticsResponse> getGeneralStatistics() {
        TransactionStatisticsResponse statistics = transactionStatisticsUseCase.getGeneralStatistics();
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "Obtener historial de transacción", description = "Recupera el historial completo y eventos de una transacción específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de transacción obtenido exitosamente", content = @Content(schema = @Schema(implementation = TransactionHistoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada"),
            @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    @GetMapping("/{id}/history")
    public ResponseEntity<TransactionHistoryResponse> getTransactionHistory(
            @Parameter(description = "ID único de la transacción", required = true) @PathVariable @NotNull @Positive Long id) {
        TransactionHistoryResponse history = getTransactionHistoryUseCase.getTransactionHistory(id);
        return ResponseEntity.ok(history);
    }

    // ===============================
    // ENDPOINTS PARA MÉTRICAS Y DASHBOARD
    // ===============================

    @Operation(summary = "Obtener métricas del sistema", description = "Genera un dashboard ejecutivo completo con métricas avanzadas del sistema de transacciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas del sistema generadas exitosamente", content = @Content(schema = @Schema(implementation = TransactionMetricsResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/metrics")
    public ResponseEntity<TransactionMetricsResponse> getSystemMetrics() {
        TransactionMetricsResponse metrics = transactionMetricsUseCase.getSystemMetrics();
        return ResponseEntity.ok(metrics);
    }

    // ===============================
    // ENDPOINTS PARA BÚSQUEDA AVANZADA
    // ===============================

    @Operation(summary = "Búsqueda avanzada de transacciones", description = "Realiza una búsqueda avanzada de transacciones con filtros múltiples y paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente", content = @Content(schema = @Schema(implementation = PagedTransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos")
    })
    @PostMapping("/search")
    public ResponseEntity<PagedTransactionResponse> searchTransactions(
            @Valid @RequestBody TransactionSearchRequest searchRequest) {
        PagedTransactionResponse result = searchTransactionsUseCase.searchTransactions(searchRequest);
        return ResponseEntity.ok(result);
    }
}
