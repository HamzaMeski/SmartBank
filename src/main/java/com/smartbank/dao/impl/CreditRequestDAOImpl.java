package com.smartbank.dao.impl;

import com.smartbank.dao.CreditRequestDAO;
import com.smartbank.model.CreditRequest;
import com.smartbank.util.EntityManagerFactorySingleton;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class CreditRequestDAOImpl implements CreditRequestDAO {
    private final EntityManagerFactory emf;

    public CreditRequestDAOImpl() {
        this.emf = EntityManagerFactorySingleton.getInstance();
    }

    @Override
    public void save(CreditRequest creditRequest) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(creditRequest);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public CreditRequest findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cr FROM CreditRequest cr LEFT JOIN FETCH cr.requestStatuses WHERE cr.id = :id",
                            CreditRequest.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<CreditRequest> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<CreditRequest> query = em.createQuery(
                    "SELECT DISTINCT cr FROM CreditRequest cr LEFT JOIN FETCH cr.requestStatuses",
                    CreditRequest.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<CreditRequest> findByDateAndStatus(LocalDateTime date, String status) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<CreditRequest> query = em.createQuery(
                    "SELECT cr FROM CreditRequest cr JOIN cr.requestStatuses rs WHERE DATE(cr.requestDate) = :date AND rs.status.name = :status",
                    CreditRequest.class);
            query.setParameter("date", date.toLocalDate());
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(CreditRequest creditRequest) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(creditRequest);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(CreditRequest creditRequest) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            CreditRequest managedCreditRequest = em.merge(creditRequest);
            em.remove(managedCreditRequest);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
