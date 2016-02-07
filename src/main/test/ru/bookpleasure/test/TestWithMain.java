package ru.bookpleasure.test;

import ru.bookpleasure.db.PersistenceManager;
import ru.bookpleasure.db.entities.Order;

/**
 * Created by Админ on 1/15/2016.
 */
public class TestWithMain {

    public static void main(String... args) {
        Order o = new Order();
        o.setId(10);
        o.setName("name");
        PersistenceManager.saveEntity(o);
        System.exit(0);
    }

}
