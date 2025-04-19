package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {
    int number;
    double saldo;
    String type;
}
