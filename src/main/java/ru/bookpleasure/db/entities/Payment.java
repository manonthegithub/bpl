package ru.bookpleasure.db.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

/**
 * Created by Kirill on 09/07/16.
 */
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "operation_id")
    private String operationId;

    @Column(name = "payment_source")
    @Enumerated(EnumType.STRING)
    private PaymentSource paymentSource;

    private String label;

    //дата создания записи в БД
    @Column(name = Constants.CREATED_AT, nullable = false)
    private Timestamp createdAt;

    //дата совершения платежа
    @Column(name = "made_at", nullable = false)
    private Timestamp madeAt;

    //Сумма, которая зачислена на счет получателя.
    @Column(scale = 2)
    private BigDecimal amount;

    //Сумма, которая списана со счета отправителя.
    @Column(name = "withdraw_amount", scale = 2)
    private BigDecimal withdrawAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "number_for_customer")
    private Order order;

    @PrePersist
    void setCreated() {
        this.createdAt = Timestamp.from(Instant.now());
        if (this.madeAt == null) {
            this.madeAt = this.createdAt;
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getMadeAt() {
        return madeAt;
    }

    public void setMadeAt(Timestamp madeAt) {
        this.madeAt = madeAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public PaymentSource getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(PaymentSource paymentSource) {
        this.paymentSource = paymentSource;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public enum Currency {
        RUR;

        public static Currency getFromCode(String code) {
            return getFromCode(Integer.valueOf(code));
        }

        public static Currency getFromCode(Integer code) {
            switch (code) {
                case 643:
                    return RUR;
                default:
                    throw new IllegalArgumentException("Валюта с таким кодом не поддерживается");
            }
        }
    }

    public enum PaymentSource {
        YANDEX
    }

}
