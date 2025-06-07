package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProfileRequest {
    private String email;
    private String name;
    private String lastName;
    private Date birthDate;
    private String gender;
}
