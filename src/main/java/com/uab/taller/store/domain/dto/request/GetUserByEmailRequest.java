package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetUserByEmailRequest {
    String email;
}
