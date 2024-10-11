package com.smartbank.business.impl;

import com.smartbank.business.CreditRequestService;
import com.smartbank.dao.CreditRequestDAO;
import com.smartbank.dao.StatusDAO;
import com.smartbank.model.CreditRequest;
import com.smartbank.model.RequestStatus;
import com.smartbank.model.Status;

import java.time.LocalDateTime;

public class CreditRequestServiceImpl implements CreditRequestService {

    private final CreditRequestDAO creditRequestDAO;
    private final StatusDAO statusDAO;

    public CreditRequestServiceImpl(CreditRequestDAO creditRequestDAO, StatusDAO statusDAO) {
        this.creditRequestDAO = creditRequestDAO;
        this.statusDAO = statusDAO;
    }

    @Override
    public void submitCreditRequest(CreditRequest creditRequest) {
        // Setting the request date
        creditRequest.setRequestDate(LocalDateTime.now());

        // Generating a unique request number
        creditRequest.setRequestNumber(generateUniqueRequestNumber());

        // Getting or creating the "PENDING" status
        Status pendingStatus = statusDAO.findByName("PENDING");
        if (pendingStatus == null) {
            pendingStatus = new Status("PENDING");
            statusDAO.save(pendingStatus);
        }

        // Creating a new RequestStatus
        RequestStatus initialStatus = new RequestStatus();
        initialStatus.setCreditRequest(creditRequest);
        initialStatus.setStatus(pendingStatus);
        initialStatus.setModificationDate(LocalDateTime.now());
        initialStatus.setExplanation("Initial submission");

        // Adding the initial status to the credit request
        creditRequest.addRequestStatus(initialStatus);

        // Saving the credit request
        creditRequestDAO.save(creditRequest);
    }

    private String generateUniqueRequestNumber() {
        // Implementing a method to generate a unique request number
        return "REQ-" + System.currentTimeMillis();
    }
}
