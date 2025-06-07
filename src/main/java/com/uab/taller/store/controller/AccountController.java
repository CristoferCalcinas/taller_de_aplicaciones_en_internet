package com.uab.taller.store.controller;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.dto.request.CreateAccountRequest;
import com.uab.taller.store.domain.dto.request.UpdateAccountRequest;
import com.uab.taller.store.domain.dto.response.AccountResponse;
import com.uab.taller.store.domain.dto.response.AccountSummaryResponse;
import com.uab.taller.store.domain.dto.response.AccountMetricsResponse;
import com.uab.taller.store.domain.dto.response.AccountActivityResponse;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.usecase.account.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Account", description = "API para gestión completa de cuentas bancarias")
public class AccountController {
    @Autowired
    private CreateAccountUseCase createAccountUseCase;

    @Autowired
    private GetAllAccountUseCase getAllAccountUseCase;

    @Autowired
    private GetAccountByIdUseCase getAccountByIdUseCase;

    @Autowired
    private GetAccountByNumberUseCase getAccountByNumberUseCase;

    @Autowired
    private GetAccountsByUserUseCase getAccountsByUserUseCase;

    @Autowired
    private GetAccountSummaryUseCase getAccountSummaryUseCase;

    @Autowired
    private DeleteAccountUseCase deleteAccountUseCase;

    @Autowired
    private UpdateAccountUseCase updateAccountUseCase;

    // Casos de uso avanzados
    @Autowired
    private FreezeAccountUseCase freezeAccountUseCase;

    @Autowired
    private BalanceOperationUseCase balanceOperationUseCase;

    @Autowired
    private AccountValidationUseCase accountValidationUseCase;

    @Autowired
    private AccountMetricsUseCase accountMetricsUseCase;

    @Autowired
    private AccountActivityUseCase accountActivityUseCase;

    @Operation(summary = "Crear una nueva cuenta", description = "Crea una nueva cuenta bancaria para un usuario")
    @PostMapping
    public ResponseEntity<Account> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        Account account = createAccountUseCase.save(request);
        accountActivityUseCase.logAccountActivity(account.getId(), "CREATION",
                "Cuenta creada exitosamente", "SYSTEM");
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @Operation(summary = "Obtener todas las cuentas", description = "Obtiene todas las cuentas activas del sistema")
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = getAllAccountUseCase.getAll();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Obtener cuenta por ID", description = "Obtiene una cuenta específica por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        Account account = getAccountByIdUseCase.getById(id);
        accountActivityUseCase.logAccountAccess(id, "WEB_BROWSER", "127.0.0.1");
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Obtener cuenta por número", description = "Obtiene una cuenta específica por su número")
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(
            @Parameter(description = "Número de la cuenta") @PathVariable String accountNumber) {
        Account account = getAccountByNumberUseCase.getByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Actualizar cuenta", description = "Actualiza los datos de una cuenta existente")
    @PutMapping
    public ResponseEntity<Account> updateAccount(
            @Valid @RequestBody UpdateAccountRequest request) {
        Account account = updateAccountUseCase.updateAccount(request);
        accountActivityUseCase.logAccountModification(account.getId(),
                "ACCOUNT_UPDATE", "Previous", "Updated");
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Eliminar cuenta (soft delete)", description = "Realiza una eliminación lógica de la cuenta")
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteAccount(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        DeleteResponse response = deleteAccountUseCase.deleteById(id);
        accountActivityUseCase.logAccountActivity(id, "DELETION",
                "Cuenta eliminada (soft delete)", "SYSTEM");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar cuenta permanentemente", description = "Realiza una eliminación física de la cuenta (solo administradores)")
    @DeleteMapping("/{id}/force")
    public ResponseEntity<DeleteResponse> forceDeleteAccount(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        DeleteResponse response = deleteAccountUseCase.forceDeleteById(id);
        accountActivityUseCase.logAccountActivity(id, "FORCE_DELETION",
                "Cuenta eliminada permanentemente", "ADMIN");
        return ResponseEntity.ok(response);
    }

    // ========== OPERACIONES POR USUARIO ==========

    @Operation(summary = "Obtener cuentas por usuario", description = "Obtiene todas las cuentas de un usuario específico")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUser(
            @Parameter(description = "ID del usuario") @PathVariable Long userId) {
        List<AccountResponse> accounts = getAccountsByUserUseCase.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Obtener cuentas activas por usuario", description = "Obtiene solo las cuentas activas de un usuario")
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<AccountResponse>> getActiveAccountsByUser(
            @Parameter(description = "ID del usuario") @PathVariable Long userId) {
        List<AccountResponse> accounts = getAccountsByUserUseCase.getActiveAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Obtener resumen de cuenta", description = "Obtiene un resumen completo de la cuenta con estadísticas")
    @GetMapping("/{id}/summary")
    public ResponseEntity<AccountSummaryResponse> getAccountSummary(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        AccountSummaryResponse summary = getAccountSummaryUseCase.getAccountSummary(id);
        return ResponseEntity.ok(summary);
    }

    @Operation(summary = "Verificar si existe número de cuenta", description = "Verifica si un número de cuenta ya existe")
    @GetMapping("/exists/{accountNumber}")
    public ResponseEntity<Boolean> accountNumberExists(
            @Parameter(description = "Número de cuenta a verificar") @PathVariable String accountNumber) {
        boolean exists = getAccountByNumberUseCase.existsByAccountNumber(accountNumber);
        return ResponseEntity.ok(exists);
    }

    // ========== OPERACIONES DE ESTADO ==========

    @Operation(summary = "Congelar cuenta", description = "Congela una cuenta para que no se puedan realizar transacciones")
    @PostMapping("/{id}/freeze")
    public ResponseEntity<Account> freezeAccount(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        Account account = freezeAccountUseCase.freezeAccount(id);
        accountActivityUseCase.logStatusChange(id, "ACTIVE", "SUSPENDED", "Manual freeze");
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Descongelar cuenta", description = "Descongela una cuenta para que se puedan realizar transacciones")
    @PostMapping("/{id}/unfreeze")
    public ResponseEntity<Account> unfreezeAccount(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        Account account = freezeAccountUseCase.unfreezeAccount(id);
        accountActivityUseCase.logStatusChange(id, "SUSPENDED", "ACTIVE", "Manual unfreeze");
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Verificar si cuenta está congelada", description = "Verifica si una cuenta está en estado suspendido")
    @GetMapping("/{id}/frozen")
    public ResponseEntity<Boolean> isAccountFrozen(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        boolean frozen = freezeAccountUseCase.isAccountFrozen(id);
        return ResponseEntity.ok(frozen);
    }

    // ========== OPERACIONES DE SALDO ==========

    @Operation(summary = "Añadir al saldo", description = "Añade una cantidad al saldo actual de la cuenta")
    @PostMapping("/{id}/balance/add")
    public ResponseEntity<Account> addToBalance(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id,
            @Parameter(description = "Monto a añadir") @RequestParam BigDecimal amount,
            @Parameter(description = "Razón del incremento") @RequestParam(required = false) String reason) {
        Account account = balanceOperationUseCase.addToBalance(id, amount, reason);
        accountActivityUseCase.logBalanceOperation(id, "ADD", amount.toString(), reason);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Restar del saldo", description = "Resta una cantidad del saldo actual de la cuenta")
    @PostMapping("/{id}/balance/subtract")
    public ResponseEntity<Account> subtractFromBalance(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id,
            @Parameter(description = "Monto a restar") @RequestParam BigDecimal amount,
            @Parameter(description = "Razón de la deducción") @RequestParam(required = false) String reason) {
        Account account = balanceOperationUseCase.subtractFromBalance(id, amount, reason);
        accountActivityUseCase.logBalanceOperation(id, "SUBTRACT", amount.toString(), reason);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Verificar saldo suficiente", description = "Verifica si una cuenta tiene saldo suficiente para una cantidad")
    @GetMapping("/{id}/balance/sufficient")
    public ResponseEntity<Boolean> hasSufficientBalance(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id,
            @Parameter(description = "Cantidad requerida") @RequestParam BigDecimal amount) {
        boolean sufficient = balanceOperationUseCase.hasSufficientBalance(id, amount);
        return ResponseEntity.ok(sufficient);
    }

    @Operation(summary = "Obtener saldo actual", description = "Obtiene el saldo actual de la cuenta")
    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getCurrentBalance(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        BigDecimal balance = balanceOperationUseCase.getCurrentBalance(id);
        return ResponseEntity.ok(balance);
    }

    @Operation(summary = "Validar creación de cuenta", description = "Valida si se puede crear una cuenta para un usuario")
    @PostMapping("/validate-creation")
    public ResponseEntity<AccountValidationUseCase.ValidationResult> validateAccountCreation(
            @Parameter(description = "ID del usuario") @RequestParam Long userId,
            @Parameter(description = "Tipo de cuenta") @RequestParam String accountType,
            @Parameter(description = "Moneda") @RequestParam String currency) {
        AccountValidationUseCase.ValidationResult result = accountValidationUseCase.validateAccountCreation(userId,
                accountType, currency);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Validar actualización de cuenta", description = "Valida si se puede actualizar una cuenta")
    @PostMapping("/{id}/validate-update")
    public ResponseEntity<AccountValidationUseCase.ValidationResult> validateAccountUpdate(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id,
            @Parameter(description = "Nuevo estado") @RequestParam(required = false) String status,
            @Parameter(description = "Nuevo saldo") @RequestParam(required = false) BigDecimal balance) {
        AccountValidationUseCase.ValidationResult result = accountValidationUseCase.validateAccountUpdate(id, status,
                balance);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Validar eliminación de cuenta", description = "Valida si se puede eliminar una cuenta")
    @PostMapping("/{id}/validate-deletion")
    public ResponseEntity<AccountValidationUseCase.ValidationResult> validateAccountDeletion(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        AccountValidationUseCase.ValidationResult result = accountValidationUseCase.validateAccountDeletion(id);
        return ResponseEntity.ok(result);
    }

    // ========== MÉTRICAS Y REPORTES ==========

    @Operation(summary = "Obtener métricas de cuenta", description = "Obtiene métricas como saldo promedio, total de transacciones, etc.")
    @GetMapping("/{id}/metrics")
    public ResponseEntity<AccountMetricsResponse> getAccountMetrics(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        AccountMetricsResponse metrics = accountMetricsUseCase.getAccountMetrics(id);
        return ResponseEntity.ok(metrics);
    }

    @Operation(summary = "Obtener métricas de todas las cuentas del usuario", description = "Obtiene métricas de todas las cuentas de un usuario")
    @GetMapping("/user/{userId}/metrics")
    public ResponseEntity<List<AccountMetricsResponse>> getUserAccountsMetrics(
            @Parameter(description = "ID del usuario") @PathVariable Long userId) {
        List<AccountMetricsResponse> metrics = accountMetricsUseCase.getUserAccountsMetrics(userId);
        return ResponseEntity.ok(metrics);
    }

    // ========== ACTIVIDAD Y AUDITORÍA ==========

    @Operation(summary = "Obtener actividad de cuenta", description = "Obtiene un resumen de la actividad reciente de la cuenta")
    @GetMapping("/{id}/activity")
    public ResponseEntity<AccountActivityResponse> getAccountActivity(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id,
            @Parameter(description = "Límite de actividades") @RequestParam(defaultValue = "10") int limit) {
        AccountActivityResponse activity = accountActivityUseCase.getAccountActivity(id, limit);
        return ResponseEntity.ok(activity);
    }

    @Operation(summary = "Obtener estadísticas de actividad", description = "Obtiene estadísticas de actividad de una cuenta en un período")
    @GetMapping("/{id}/activity/stats")
    public ResponseEntity<AccountActivityResponse.ActivityStats> getActivityStats(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id,
            @Parameter(description = "Días hacia atrás") @RequestParam(defaultValue = "30") int days) {
        AccountActivityResponse.ActivityStats stats = accountActivityUseCase.getActivityStats(id, days);
        return ResponseEntity.ok(stats);
    }
}
