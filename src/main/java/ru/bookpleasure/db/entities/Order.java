package ru.bookpleasure.db.entities;

import ru.bookpleasure.db.Persistable;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by Админ on 1/14/2016.
 */
@Entity
@Table(name = "orders")
public class Order implements Persistable {
    public Order() {
    }

    public Order(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


}
