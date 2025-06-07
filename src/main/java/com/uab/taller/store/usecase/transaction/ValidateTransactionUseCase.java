package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.dto.request.TransferRequest;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ValidateTransactionUseCase {
    @Autowired
    private IAccountService accountService;

    public boolean validateTransfer(TransferRequest transferRequest) {
        try {
            Account sourceAccount = accountService.findById(transferRequest.getSourceAccountId());
            Account targetAccount = accountService.findById(transferRequest.getTargetAccountId());

            // Validar que las cuentas existan
            if (sourceAccount == null || targetAccount == null) {
                return false;
            }

            // Validar que las cuentas sean diferentes
            if (sourceAccount.getId().equals(targetAccount.getId())) {
                return false;
            }

            // Validar que la cuenta origen tenga saldo suficiente
            if (sourceAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
                return false;
            }

            // Validar que el monto sea positivo
            if (transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }

            // Validar que las cuentas estén activas
            if (!"ACTIVE".equals(sourceAccount.getStatus()) || !"ACTIVE".equals(targetAccount.getStatus())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateDeposit(Long accountId, BigDecimal amount) {
        try {
            Account account = accountService.findById(accountId);

            if (account == null) {
                return false;
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }

            if (!"ACTIVE".equals(account.getStatus())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateWithdrawal(Long accountId, BigDecimal amount) {
        try {
            Account account = accountService.findById(accountId);

            if (account == null) {
                return false;
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }

            if (account.getBalance().compareTo(amount) < 0) {
                return false;
            }

            if (!"ACTIVE".equals(account.getStatus())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
