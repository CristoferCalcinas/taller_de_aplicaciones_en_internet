package com.uab.taller.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transaction")
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id", nullable = false, referencedColumnName = "account_id", foreignKey = @ForeignKey(name = "FK_Transaction_SourceAccount"))
    private Account sourceAccount;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_account_id", nullable = true, referencedColumnName = "account_id", foreignKey = @ForeignKey(name = "FK_Transaction_TargetAccount"))
    private Account targetAccount;

    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime date;

    @Column(length = 255)
    private String description;

    @Column(length = 100)
    private String reference;

    @Column(length = 500)
    private String notes;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "COMPLETED";

    @Column(length = 10, nullable = false)
    private String currency = "BOB";
}