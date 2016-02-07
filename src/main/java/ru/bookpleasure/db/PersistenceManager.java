package ru.bookpleasure.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.bookpleasure.db.entities.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * Created by Админ on 1/14/2016.
 */
public class PersistenceManager {

    private static final EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("bpl");
    private static final EntityManager entitymanager = emfactory.createEntityManager();

    public static void saveEntity(Persistable entity) {
        entitymanager.getTransaction().begin();
        entitymanager.persist(entity);
        entitymanager.getTransaction().commit();
    }

}
