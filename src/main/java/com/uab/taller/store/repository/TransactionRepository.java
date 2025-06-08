package com.uab.taller.store.repository;

import com.uab.taller.store.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

       // Buscar transacciones por cuenta (origen o destino)
       @Query("SELECT t FROM Transaction t WHERE t.sourceAccount.id = :accountId OR t.targetAccount.id = :accountId")
       List<Transaction> findByAccountId(@Param("accountId") Long accountId);

       // Buscar transacciones por cuenta con paginación
       @Query("SELECT t FROM Transaction t WHERE t.sourceAccount.id = :accountId OR t.targetAccount.id = :accountId")
       Page<Transaction> findByAccountId(@Param("accountId") Long accountId, Pageable pageable);

       // Buscar transacciones por tipo
       List<Transaction> findByTransactionTypeIgnoreCase(String transactionType);

       // Buscar transacciones por tipo con paginación
       Page<Transaction> findByTransactionTypeIgnoreCase(String transactionType, Pageable pageable);

       // Buscar transacciones por rango de fechas
       List<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

       // Buscar transacciones por rango de fechas con paginación
       Page<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

       // Buscar transacciones por cuenta origen
       List<Transaction> findBySourceAccountId(Long sourceAccountId);

       // Buscar transacciones por cuenta destino
       List<Transaction> findByTargetAccountId(Long targetAccountId);

       // Métodos avanzados para filtros combinados

       // Buscar por cuenta, tipo y rango de fechas
       @Query("SELECT t FROM Transaction t WHERE (t.sourceAccount.id = :accountId OR t.targetAccount.id = :accountId) "
                     +
                     "AND t.transactionType = :type AND t.date BETWEEN :startDate AND :endDate")
       Page<Transaction> findByAccountIdAndTypeAndDateRange(@Param("accountId") Long accountId,
                     @Param("type") String type,
                     @Param("startDate") LocalDateTime startDate,
                     @Param("endDate") LocalDateTime endDate,
                     Pageable pageable);

       // Buscar por cuenta y rango de fechas
       @Query("SELECT t FROM Transaction t WHERE (t.sourceAccount.id = :accountId OR t.targetAccount.id = :accountId) "
                     +
                     "AND t.date BETWEEN :startDate AND :endDate")
       Page<Transaction> findByAccountIdAndDateRange(@Param("accountId") Long accountId,
                     @Param("startDate") LocalDateTime startDate,
                     @Param("endDate") LocalDateTime endDate,
                     Pageable pageable);

       // Buscar por cuenta y tipo
       @Query("SELECT t FROM Transaction t WHERE (t.sourceAccount.id = :accountId OR t.targetAccount.id = :accountId) "
                     +
                     "AND t.transactionType = :type")
       Page<Transaction> findByAccountIdAndType(@Param("accountId") Long accountId,
                     @Param("type") String type,
                     Pageable pageable);

       // Buscar por tipo y rango de fechas
       @Query("SELECT t FROM Transaction t WHERE t.transactionType = :type AND t.date BETWEEN :startDate AND :endDate")
       Page<Transaction> findByTypeAndDateRange(@Param("type") String type,
                     @Param("startDate") LocalDateTime startDate,
                     @Param("endDate") LocalDateTime endDate,
                     Pageable pageable);

       // Buscar por descripción o referencia (búsqueda de texto)
       @Query("SELECT t FROM Transaction t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
                     "OR LOWER(t.reference) LIKE LOWER(CONCAT('%', :searchText, '%'))")
       List<Transaction> findByDescriptionContainingIgnoreCaseOrReferenceContainingIgnoreCase(
                     @Param("searchText") String searchText);
}
