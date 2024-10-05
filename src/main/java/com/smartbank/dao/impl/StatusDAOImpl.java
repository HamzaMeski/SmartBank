package com.smartbank.dao.impl;

import com.smartbank.dao.StatusDAO;
import com.smartbank.model.Status;
import com.smartbank.util.EntityManagerFactorySingleton;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class StatusDAOImpl implements StatusDAO {

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
