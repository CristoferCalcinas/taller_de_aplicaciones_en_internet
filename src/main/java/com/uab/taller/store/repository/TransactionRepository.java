package com.uab.taller.store.repository;

import com.uab.taller.store.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Buscar transacciones por cuenta (origen o destino)
    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount.id = :accountId OR t.targetAccount.id = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);

    // Buscar transacciones por tipo
    List<Transaction> findByTransactionTypeIgnoreCase(String transactionType);

    List<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Buscar transacciones por cuenta origen
    List<Transaction> findBySourceAccountId(Long sourceAccountId);

    // Buscar transacciones por cuenta destino
    List<Transaction> findByTargetAccountId(Long targetAccountId);
}
