package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Getter
public class CreateUserRequest {
    String email;
    String password;
    String name;
    String lastName;
    String gender;
    Date birthDate;
}
