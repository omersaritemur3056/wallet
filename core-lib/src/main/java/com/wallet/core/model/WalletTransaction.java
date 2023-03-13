package com.wallet.core.model;

import com.wallet.core.enums.TransactionStatus;
import com.wallet.core.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_transaction_gen")
    @SequenceGenerator(name = "wallet_transaction_gen", sequenceName = "wallet_transaction_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID transactionId = UUID.randomUUID();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus = TransactionStatus.PROCESSING;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet senderWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet receiverWallet;
}