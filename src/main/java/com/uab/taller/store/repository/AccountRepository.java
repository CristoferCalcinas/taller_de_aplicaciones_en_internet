package com.uab.taller.store.repository;

import com.uab.taller.store.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Busca una cuenta por su número de cuenta
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Busca cuentas por ID de usuario
     */
    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    List<Account> findByUserId(@Param("userId") Long userId);

    /**
     * Busca cuentas por ID de usuario y estado
     */
    @Query("SELECT a FROM Account a WHERE a.user.id = :userId AND a.status = :status")
    List<Account> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * Busca cuentas por tipo
     */
    List<Account> findByType(String type);

    /**
     * Busca cuentas por estado
     */
    List<Account> findByStatus(String status);

    /**
     * Busca cuentas activas de un usuario
     */
    @Query("SELECT a FROM Account a WHERE a.user.id = :userId AND a.status = 'ACTIVE' AND a.deleted = false")
    List<Account> findActiveAccountsByUserId(@Param("userId") Long userId);

    /**
     * Cuenta el número de cuentas activas de un usuario
     */
    @Query("SELECT COUNT(a) FROM Account a WHERE a.user.id = :userId AND a.status = 'ACTIVE' AND a.deleted = false")
    Long countActiveAccountsByUserId(@Param("userId") Long userId);

    /**
     * Verifica si existe una cuenta con el número dado
     */
    boolean existsByAccountNumber(String accountNumber);
}
