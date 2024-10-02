package com.smartbank.dao;

import com.smartbank.model.CreditRequest;
import java.util.Date;
import java.util.List;

public interface CreditRequestDAO {
    void save(CreditRequest request);
    CreditRequest findByNumero(String numero);
    List<CreditRequest> findAll();
    List<CreditRequest> findByDateAndStatus(Date date, String status);
    void updateStatus(String numero, String newStatus);
}
