package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.request.WithdrawalRequest;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProcessWithdrawalUseCase {
    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ValidateTransactionUseCase validateTransactionUseCase;

    @Transactional
    public Transaction processWithdrawal(WithdrawalRequest withdrawalRequest) {
        // Validar el retiro
        if (!validateTransactionUseCase.validateWithdrawal(withdrawalRequest.getAccountId(),
                withdrawalRequest.getAmount())) {
            throw new RuntimeException("Retiro inválido: verificar cuenta, saldo o datos de la transacción");
        }

        // Obtener la cuenta
        Account account = accountService.findById(withdrawalRequest.getAccountId());

        // Validar que la cuenta tenga saldo suficiente
        if (account.getBalance().compareTo(withdrawalRequest.getAmount()) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar el retiro");
        }

        // Actualizar saldo
        account.setBalance(account.getBalance().subtract(withdrawalRequest.getAmount()));
        accountService.update(account);

        // Crear y guardar la transacción
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(account);
        transaction.setAmount(withdrawalRequest.getAmount());
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(withdrawalRequest.getDescription());
        transaction.setReference(withdrawalRequest.getReference());
        transaction.setStatus("COMPLETED");
        transaction.setCurrency("BOB");

        return transactionService.save(transaction);
    }
}
