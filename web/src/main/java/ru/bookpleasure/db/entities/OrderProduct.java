package ru.bookpleasure.db.entities;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by Kirill on 08/07/16.
 */
@Entity
@Table(name = "order_products")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id")
    private Product product;

    //количество единиц товара в заказе
    @Column(nullable = false)
    private int number;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order linkedOrder;

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
        return linkedOrder;
    }

    public void setOrder(Order order) {
        this.linkedOrder = order;
    }
}
