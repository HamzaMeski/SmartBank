package com.smartbank.business;

import com.smartbank.exception.ValidationException;
import com.smartbank.model.CreditRequest;

import java.util.List;
import java.time.LocalDateTime;

public interface CreditRequestService {
    void submitCreditRequest(CreditRequest creditRequest) throws ValidationException;
    List<CreditRequest> getAllCreditRequests();
    CreditRequest getCreditRequestDetails(Long id);
    void updateCreditRequestStatus(Long id, String newStatus);
    List<CreditRequest> filterCreditRequests(LocalDateTime date, String status);
}
