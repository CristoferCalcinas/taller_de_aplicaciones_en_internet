package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeneficiaryRequest {
    private Long userId;
    private Long accountId;
    private String alias;
    private String description;
}
