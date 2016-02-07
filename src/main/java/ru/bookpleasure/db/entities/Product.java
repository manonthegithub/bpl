package ru.bookpleasure.db.entities;

import ru.bookpleasure.db.Persistable;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by Админ on 2/1/2016.
 */
@Entity
public class Product implements Persistable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;
    public String name;
    public String description;
    public int quantity;
    public boolean enabled;

    @Enumerated(EnumType.STRING)
    public ProductCategory category;

    public enum ProductCategory {
        BOOKBOX;
    }
}
