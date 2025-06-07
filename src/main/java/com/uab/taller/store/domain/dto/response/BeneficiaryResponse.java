package com.uab.taller.store.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryResponse {
    private Long id;
    private String alias;
    private String description;
    private LocalDateTime addDate;
    private String addUser;
    private LocalDateTime changeDate;
    private String changeUser;
    private boolean deleted;

    // Información básica del usuario
    private Long userId;
    private String userEmail;

    // Información básica de la cuenta
    private Long accountId;
    private String accountNumber;
    private String accountType;
    private String accountCurrency;
    private String accountStatus;
}
