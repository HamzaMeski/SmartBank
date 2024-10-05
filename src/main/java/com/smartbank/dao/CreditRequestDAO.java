package com.smartbank.dao;

import com.smartbank.model.CreditRequest;
import java.util.List;
import java.util.Date;

public interface CreditRequestDAO {
    void save(CreditRequest creditRequest);
    CreditRequest findById(Long id);
    List<CreditRequest> findAll();
    List<CreditRequest> findByDateAndStatus(Date date, String status);
    void update(CreditRequest creditRequest);
    void delete(CreditRequest creditRequest);
}
