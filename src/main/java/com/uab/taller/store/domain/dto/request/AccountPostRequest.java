package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountPostRequest {
    private Long id;
    private Double saldo;
    private String type;
    private Long userId;
}
