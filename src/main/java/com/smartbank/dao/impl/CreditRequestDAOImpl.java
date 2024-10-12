package com.smartbank.dao.impl;

import com.smartbank.dao.CreditRequestDAO;
import com.smartbank.model.CreditRequest;
import com.smartbank.util.EntityManagerFactorySingleton;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class CreditRequestDAOImpl implements CreditRequestDAO {
    private EntityManager entityManager;

    public CreditRequestDAOImpl() {
        this.entityManager = EntityManagerFactorySingleton.getInstance().createEntityManager();
    }

    @Override
    public void save(CreditRequest creditRequest) {
        entityManager.getTransaction().begin();
        entityManager.persist(creditRequest);
        entityManager.getTransaction().commit();
    }

    @Override
    public CreditRequest findById(Long id) {
        return entityManager.find(CreditRequest.class, id);
    }

    @Override
    public List<CreditRequest> findAll() {
        TypedQuery<CreditRequest> query = entityManager.createQuery("SELECT cr FROM CreditRequest cr", CreditRequest.class);
        return query.getResultList();
    }

    @Override
    public List<CreditRequest> findByDateAndStatus(LocalDateTime date, String status) {
        TypedQuery<CreditRequest> query = entityManager.createQuery(
                "SELECT cr FROM CreditRequest cr JOIN cr.requestStatuses rs WHERE DATE(cr.requestDate) = :date AND rs.status = :status",
                CreditRequest.class);
        query.setParameter("date", date.toLocalDate());
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public void update(CreditRequest creditRequest) {
        entityManager.getTransaction().begin();
        entityManager.merge(creditRequest);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(CreditRequest creditRequest) {
        entityManager.getTransaction().begin();
        entityManager.remove(creditRequest);
        entityManager.getTransaction().commit();
    }
}
