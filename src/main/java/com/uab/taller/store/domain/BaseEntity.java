package com.uab.taller.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(name = "add_date", nullable = false, updatable = false)
    private LocalDateTime addDate;

    @Column(name = "add_user", length = 50)
    private String addUser;

    @UpdateTimestamp
    @Column(name = "change_date")
    private LocalDateTime changeDate;

    @Column(name = "change_user", length = 50)
    private String changeUser;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
}