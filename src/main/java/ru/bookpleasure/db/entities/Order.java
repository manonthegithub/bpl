package ru.bookpleasure.db.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Created by Админ on 1/14/2016.
 */
@Entity
@Table(name = "orders")
@SequenceGenerator(name = "order_number_seq", initialValue = 1, allocationSize = 100)
public class Order {

    //человекочитаемый идентификатор для отслеживания клиентом
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_number_seq")
    @Column(name = "number_for_customer")
    private Long numberForCustomer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    //итого к оплате с вычетом скидок
    @Column(name = "total_amount", scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "linkedOrder", fetch = FetchType.LAZY)
    private List<OrderProduct> products;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "order")
    private Payment payment;

    @Column(name = Constants.CREATED_AT, nullable = false)
    private Timestamp createdAt;

    @Column(name = Constants.UPDATED_AT, nullable = false)
    private Timestamp updatedAt;

    @Embedded
    private Address address;

    @Embedded
    private CustomerDetails customerDetails;

    public Order() {
    }

    public Order(Long number) {
        this();
        this.numberForCustomer = number;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = this.updatedAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public Long getNumberForCustomer() {
        return numberForCustomer;
    }

    public void setNumberForCustomer(Long numberForCustomer) {
        this.numberForCustomer = numberForCustomer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        if (totalAmount.compareTo(new BigDecimal(0)) > 0) {
            this.totalAmount = totalAmount;
        } else {
            throw new IllegalArgumentException("Сумма заказа не может быть отрицательной");
        }
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }


    public enum Status {
        EDITING {
            @Override
            public String readableValue() {
                return "Корзина";
            }

        },
        AWAITING_PAYMENT {
            @Override
            public String readableValue() {
                return "Ожидает поступления оплаты";
            }

            @Override
            public Boolean paymentNeeded() {
                return true;
            }
        },
        PAID {
            @Override
            public String readableValue() {
                return "Оплата получена, ожидается отправка";
            }
        },
        SENT {
            @Override
            public String readableValue() {
                return "Отправлен";
            }
        },
        FINISHED {
            @Override
            public String readableValue() {
                return "Доставлен получателю";
            }
        },
        CANCELLED {
            @Override
            public String readableValue() {
                return "Отменён";
            }
        };

        public abstract String readableValue();


        public Boolean paymentNeeded() {
            return false;
        }
    }
}
