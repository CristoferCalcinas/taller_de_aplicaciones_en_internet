package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProfileRequest {
    String email;
    String name;
    String lastName;
    Date birthDate;
    String gender;
}
