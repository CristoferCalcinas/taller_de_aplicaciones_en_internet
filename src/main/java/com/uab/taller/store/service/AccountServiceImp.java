package com.uab.taller.store.service;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.repository.AccountRepository;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImp implements IAccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImp(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll().stream()
                .filter(account -> !account.isDeleted())
                .toList();
    }

    @Override
    public Account save(Account account) {
        if (account.getId() == null) {
            // Nueva cuenta
            if (account.getAccountNumber() == null || account.getAccountNumber().isEmpty()) {
                account.setAccountNumber(generateAccountNumber());
            }
            if (account.getStatus() == null) {
                account.setStatus("ACTIVE");
            }
            if (account.getBalance() == null) {
                account.getBalance().equals(BigDecimal.ZERO);
            }
            account.setAddDate(LocalDateTime.now());
            account.setAddUser("SYSTEM");
        } else {
            // Actualización
            account.setChangeDate(LocalDateTime.now());
            account.setChangeUser("SYSTEM");
        }
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .filter(account -> !account.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta", id));
    }

    @Override
    public Account update(Account account) {
        if (account.getId() == null) {
            throw new IllegalArgumentException("El ID de la cuenta es requerido para actualizar");
        }

        Account existingAccount = findById(account.getId());

        // Actualizar solo los campos permitidos
        if (account.getType() != null) {
            existingAccount.setType(account.getType());
        }
        if (account.getCurrency() != null) {
            existingAccount.setCurrency(account.getCurrency());
        }
        if (account.getStatus() != null) {
            existingAccount.setStatus(account.getStatus());
        }
        if (account.getBalance() != null) {
            existingAccount.setBalance(account.getBalance());
        }

        existingAccount.setChangeDate(LocalDateTime.now());
        existingAccount.setChangeUser("SYSTEM");

        return accountRepository.save(existingAccount);
    }

    @Override
    public void deleteById(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new EntityNotFoundException("Cuenta", id);
        }
        try {
            accountRepository.deleteById(id);
        } catch (Exception e) {
            throw new EntityDeletionException("Cuenta", id, "Error durante la eliminación: " + e.getMessage());
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .filter(account -> !account.isDeleted());
    }

    @Override
    public List<Account> findAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .filter(account -> !account.isDeleted())
                .toList();
    }

    @Override
    public List<Account> findActiveAccountsByUserId(Long userId) {
        return accountRepository.findByUserIdAndStatus(userId, "ACTIVE").stream()
                .filter(account -> !account.isDeleted())
                .toList();
    }

    @Override
    public boolean isAccountActive(Long accountId) {
        return accountRepository.findById(accountId)
                .map(account -> "ACTIVE".equals(account.getStatus()) && !account.isDeleted())
                .orElse(false);
    }

    @Override
    public Account updateBalance(Long accountId, BigDecimal newBalance) {
        Account account = findById(accountId);
        account.setBalance(newBalance);
        account.setChangeDate(LocalDateTime.now());
        account.setChangeUser("SYSTEM_BALANCE_UPDATE");
        return accountRepository.save(account);
    }

    @Override
    public String generateAccountNumber() {
        String prefix = "ACC";
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(6);
        String randomPart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6).toUpperCase();
        return prefix + timestamp + randomPart;
    }

    @Override
    public List<Account> findAccountsByType(String type) {
        return accountRepository.findByType(type).stream()
                .filter(account -> !account.isDeleted())
                .toList();
    }

    @Override
    public List<Account> findAccountsByStatus(String status) {
        return accountRepository.findByStatus(status).stream()
                .filter(account -> !account.isDeleted())
                .toList();
    }
}
