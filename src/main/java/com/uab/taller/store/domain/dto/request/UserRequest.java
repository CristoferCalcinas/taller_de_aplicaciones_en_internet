package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    String name;
    String lastName;
    String email;
    String password;
}
