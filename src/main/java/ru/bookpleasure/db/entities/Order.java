package ru.bookpleasure.db.entities;

import ru.bookpleasure.db.Persistable;

import javax.persistence.*;

/**
 * Created by Админ on 1/14/2016.
 */
@Entity
@Table(name = "orderses")
public class Order implements Persistable {
    public Order() {
    }

    public Order(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
