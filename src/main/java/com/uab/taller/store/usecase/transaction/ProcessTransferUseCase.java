package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.request.TransferRequest;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProcessTransferUseCase {
    @Autowired
    private ITransactionService transactionService;
    
    @Autowired
    private IAccountService accountService;    @Transactional
    public Transaction processTransfer(TransferRequest transferRequest) {
        // Obtener las cuentas
        Account sourceAccount = accountService.findById(transferRequest.getSourceAccountId());
        Account targetAccount = accountService.findById(transferRequest.getTargetAccountId());
        
        // Validar que la cuenta origen tenga saldo suficiente
        if (sourceAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new RuntimeException("Saldo insuficiente en la cuenta origen");
        }
        
        // Actualizar saldos
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferRequest.getAmount()));
        targetAccount.setBalance(targetAccount.getBalance().add(transferRequest.getAmount()));
        
        // Guardar las cuentas actualizadas
        accountService.update(sourceAccount);
        accountService.update(targetAccount);
        
        // Crear y guardar la transacción
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setAmount(transferRequest.getAmount());
        transaction.setTransactionType("TRANSFER");
        transaction.setDate(LocalDateTime.now());
        
        return transactionService.save(transaction);
    }
}
