package com.smartbank.dao.impl;

import com.smartbank.dao.CreditRequestDAO;
import com.smartbank.dao.StatusDAO;
import com.smartbank.model.Status;
import com.smartbank.util.EntityManagerFactorySingleton;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.logging.Logger;

public class StatusDAOImpl implements StatusDAO {
    private static final Logger LOGGER = Logger.getLogger(CreditRequestDAO.class.getCanonicalName());

    private EntityManager entityManager;

    public StatusDAOImpl() {
        this.entityManager = EntityManagerFactorySingleton.getInstance().createEntityManager();
    }

    @Override
    public Status findByName(String name) {
        TypedQuery<Status> query = entityManager.createQuery(
                "SELECT s FROM Status s WHERE s.name = :name", Status.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Status status) {
        entityManager.getTransaction().begin();
        entityManager.persist(status);
        entityManager.getTransaction().commit();
    }
}
