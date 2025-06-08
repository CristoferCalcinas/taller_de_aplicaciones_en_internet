package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.request.DepositRequest;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProcessDepositUseCase {
    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ValidateTransactionUseCase validateTransactionUseCase;

    @Transactional
    public Transaction processDeposit(DepositRequest depositRequest) {
        // Validar el depósito
        if (!validateTransactionUseCase.validateDeposit(depositRequest.getAccountId(), depositRequest.getAmount())) {
            throw new RuntimeException("Depósito inválido: verificar cuenta y datos de la transacción");
        }

        // Obtener la cuenta
        Account account = accountService.findById(depositRequest.getAccountId());

        // Actualizar saldo
        account.setBalance(account.getBalance().add(depositRequest.getAmount()));
        accountService.update(account);

        // Crear y guardar la transacción
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(account);
        transaction.setAmount(depositRequest.getAmount());
        transaction.setTransactionType("DEPOSIT");
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(depositRequest.getDescription());
        transaction.setReference(depositRequest.getReference());
        transaction.setStatus("COMPLETED");
        transaction.setCurrency("BOB");

        return transactionService.save(transaction);
    }
}
