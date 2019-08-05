package org.fchiotta.accounttransactions.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {

    private static final EntityManager entityManager = buildEntityManager();

    private static EntityManager buildEntityManager() {
        try {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("AccountTransactions");
            return entityManagerFactory.createEntityManager();
        }
        catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

}
