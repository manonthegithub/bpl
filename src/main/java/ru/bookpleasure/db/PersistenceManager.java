package ru.bookpleasure.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.bookpleasure.db.entities.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.text.html.parser.Entity;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * Created by Админ on 1/14/2016.
 */
public class PersistenceManager {

    private static final EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("bpl");

    public static void saveEntity(Persistable entity) {
        EntityManager entitymanager = emfactory.createEntityManager();
        entitymanager.getTransaction().begin();
        entitymanager.persist(entity);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }

    public static EntityManager getEntityManager(){
        return emfactory.createEntityManager();
    }

}
