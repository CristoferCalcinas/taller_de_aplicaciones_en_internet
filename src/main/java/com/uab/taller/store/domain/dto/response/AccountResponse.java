package com.uab.taller.store.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String currency;
    private String type;
    private BigDecimal balance;
    private String status;

    // Información del usuario propietario
    private Long userId;
    private String userEmail;
    private String userName;
    private String userLastName;

    // Información de auditoría
    private LocalDateTime addDate;
    private LocalDateTime changeDate;
    private String addUser;
    private String changeUser;

    // Estadísticas de la cuenta
    private Integer totalTransactions;
    private Integer activeBeneficiaries;

    // Información adicional para el dashboard
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
}
