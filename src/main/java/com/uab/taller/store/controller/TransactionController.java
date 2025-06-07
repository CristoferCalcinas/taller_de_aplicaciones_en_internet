package com.uab.taller.store.controller;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.request.DepositRequest;
import com.uab.taller.store.domain.dto.request.TransferRequest;
import com.uab.taller.store.domain.dto.request.WithdrawalRequest;
import com.uab.taller.store.usecase.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    CreateTransactionUseCase createTransactionUseCase;

    @Autowired
    GetAllTransactionsUseCase getAllTransactionsUseCase;

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

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return createTransactionUseCase.save(transaction);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return getAllTransactionsUseCase.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return getTransactionByIdUseCase.getById(id);
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> getTransactionsByAccount(@PathVariable Long accountId) {
        return getTransactionsByAccountUseCase.getTransactionsByAccount(accountId);
    }

    @DeleteMapping("/{id}")
    public void deleteTransactionById(@PathVariable Long id) {
        deleteTransactionUseCase.deleteById(id);
    }

    @PutMapping
    public Transaction updateTransaction(@RequestBody Transaction transaction) {
        return updateTransactionUseCase.update(transaction);
    }

    // Endpoints específicos para operaciones financieras
    @PostMapping("/transfer")
    public Transaction processTransfer(@RequestBody TransferRequest transferRequest) {
        return processTransferUseCase.processTransfer(transferRequest);
    }

    @PostMapping("/deposit")
    public Transaction processDeposit(@RequestBody DepositRequest depositRequest) {
        return processDepositUseCase.processDeposit(depositRequest);
    }

    @PostMapping("/withdrawal")
    public Transaction processWithdrawal(@RequestBody WithdrawalRequest withdrawalRequest) {
        return processWithdrawalUseCase.processWithdrawal(withdrawalRequest);
    }

    // Endpoints adicionales para consultas específicas
    @GetMapping("/date-range")
    public List<Transaction> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return getTransactionsByDateRangeUseCase.getTransactionsByDateRange(startDate, endDate);
    }

    @GetMapping("/type/{type}")
    public List<Transaction> getTransactionsByType(@PathVariable String type) {
        return getTransactionsByTypeUseCase.getTransactionsByType(type);
    }
}
