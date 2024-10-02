package com.smartbank.business;

import com.smartbank.model.CreditRequest;
import java.util.Date;
import java.util.List;

public interface CreditService {
    void submitCreditRequest(CreditRequest request);
    CreditRequest getCreditRequest(String numero);
    List<CreditRequest> getAllCreditRequests();
    List<CreditRequest> getCreditRequestsByDateAndStatus(Date date, String status);
    void updateCreditRequestStatus(String numero, String newStatus);
    double calculateMonthlyPayment(double amount, int duration, double interestRate);
}
