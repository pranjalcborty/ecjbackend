package db.utils;

import db.models.CommonParent;
import db.models.Config;
import db.models.Status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;

import static javax.persistence.Persistence.createEntityManagerFactory;

public class JPAUtil {

    private static final EntityManagerFactory entityManagerFactory =
            createEntityManagerFactory("persistence");

    public void save(CommonParent obj) {
        try {
            EntityManager em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();

            obj.setUploadedOn(new Date());
            em.persist(obj);

            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Object obj) {
        try {
            EntityManager em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();

            em.merge(obj);

            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Object obj) {
        try {
            EntityManager em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();

            em.remove(obj);

            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Config getNextTask() {
        String jpql = "FROM Config c" +
                " WHERE c.status = :status" +
                " ORDER BY c.uploadedOn ASC";

        try {
            EntityManager em = entityManagerFactory.createEntityManager();

            return em.createQuery(jpql, Config.class)
                    .setParameter("status", Status.PENDING)
                    .setMaxResults(1)
                    .getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
