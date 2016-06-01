package ru.bookpleasure.db.entities;
import javax.persistence.*;
import java.util.UUID;

/**
 * Created by Админ on 2/1/2016.
 */
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;
    public String name;
    public String description;
    public int quantity;
    public int availableNumber;
    public boolean enabled;
    public String imageFilename;
    public int price;

    @Enumerated(EnumType.STRING)
    public ProductCategory category;

    public enum ProductCategory {
        BOOKBOX;
    }
}
