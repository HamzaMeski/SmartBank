package com.smartbank.business;

import com.smartbank.exception.ValidationException;
import com.smartbank.model.CreditRequest;

public interface CreditRequestService {
    void submitCreditRequest(CreditRequest creditRequest) throws ValidationException;
}
