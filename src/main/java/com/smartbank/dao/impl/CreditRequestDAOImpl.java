package com.smartbank.dao.impl;

import com.smartbank.dao.CreditRequestDAO;
import com.smartbank.model.CreditRequest;
import com.smartbank.util.EntityManagerFactorySingleton;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class CreditRequestDAOImpl implements CreditRequestDAO {
    private EntityManager em;

    public CreditRequestDAOImpl() {
        this.em = EntityManagerFactorySingleton.getInstance().createEntityManager();
    }

    @Override
    public void save(CreditRequest request) {
        em.getTransaction().begin();
        em.persist(request);
        em.getTransaction().commit();
    }

    @Override
    public CreditRequest findByNumero(String numero) {
        TypedQuery<CreditRequest> query = em.createQuery(
                "SELECT c FROM CreditRequest c WHERE c.numero = :numero", CreditRequest.class);
        query.setParameter("numero", numero);
        List<CreditRequest> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<CreditRequest> findAll() {
        TypedQuery<CreditRequest> query = em.createQuery("SELECT c FROM CreditRequest c", CreditRequest.class);
        return query.getResultList();
    }

    @Override
    public List<CreditRequest> findByDateAndStatus(Date date, String status) {
        TypedQuery<CreditRequest> query = em.createQuery(
                "SELECT c FROM CreditRequest c WHERE c.date = :date AND c.etat = :status", CreditRequest.class);
        query.setParameter("date", date);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public void updateStatus(String numero, String newStatus) {
        em.getTransaction().begin();
        CreditRequest request = findByNumero(numero);
        if (request != null) {
            request.setEtat(CreditStatus.valueOf(newStatus));
        }
        em.getTransaction().commit();
    }
}
