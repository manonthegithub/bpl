package ru.bookpleasure.db.entities;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by Kirill on 08/07/16.
 */
@Entity
@Table(name = "order-products")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Product product;

    @Column(nullable = false)
    private int number;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order-id")
    private Order order;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
