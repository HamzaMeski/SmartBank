package com.smartbank.dao;

import com.smartbank.model.CreditRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface CreditRequestDAO {
    void save(CreditRequest creditRequest);
    CreditRequest findById(Long id);
    List<CreditRequest> findAll();
    List<CreditRequest> findByDateAndStatus(LocalDateTime date, String status);
    void update(CreditRequest creditRequest);
    void delete(CreditRequest creditRequest);
}
