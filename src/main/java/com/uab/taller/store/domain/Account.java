package com.uab.taller.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int number;
    double saldo;
    String type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
