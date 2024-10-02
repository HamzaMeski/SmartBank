package com.smartbank.business;

import com.smartbank.dao.CreditRequestDAO;
import com.smartbank.model.CreditRequest;

import java.util.Date;
import java.util.List;

public class CreditServiceImpl implements CreditService {
    private CreditRequestDAO creditRequestDAO;

    public CreditServiceImpl(CreditRequestDAO creditRequestDAO) {
        this.creditRequestDAO = creditRequestDAO;
    }

    @Override
    public void submitCreditRequest(CreditRequest request) {
        creditRequestDAO.save(request);
    }

    @Override
    public CreditRequest getCreditRequest(String numero) {
        return creditRequestDAO.findByNumero(numero);
    }

    @Override
    public List<CreditRequest> getAllCreditRequests() {
        return creditRequestDAO.findAll();
    }

    @Override
    public List<CreditRequest> getCreditRequestsByDateAndStatus(Date date, String status) {
        return creditRequestDAO.findByDateAndStatus(date, status);
    }

    @Override
    public void updateCreditRequestStatus(String numero, String newStatus) {
        creditRequestDAO.updateStatus(numero, newStatus);
    }

    @Override
    public double calculateMonthlyPayment(double amount, int duration, double interestRate) {
        double monthlyRate = interestRate / 12 / 100;
        return (amount * monthlyRate * Math.pow(1 + monthlyRate, duration)) / (Math.pow(1 + monthlyRate, duration) - 1);
    }
}
