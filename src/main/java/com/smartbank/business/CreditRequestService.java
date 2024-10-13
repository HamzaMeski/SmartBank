package com.smartbank.business;

import com.smartbank.exception.ValidationException;
import com.smartbank.model.CreditRequest;

import java.util.List;

public interface CreditRequestService {
    void submitCreditRequest(CreditRequest creditRequest) throws ValidationException;
    List<CreditRequest> getAllCreditRequests();
    CreditRequest getCreditRequestDetails(Long id);
    void updateCreditRequestStatus(Long id, String newStatus);
}
