package com.smartbank.business.impl;

import com.smartbank.business.CreditRequestService;
import com.smartbank.dao.CreditRequestDAO;
import com.smartbank.dao.StatusDAO;
import com.smartbank.model.CreditRequest;
import com.smartbank.model.RequestStatus;
import com.smartbank.model.Status;
import com.smartbank.exception.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.regex.Pattern;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreditRequestServiceImpl implements CreditRequestService {

    @Inject
    private CreditRequestDAO creditRequestDAO;
    @Inject
    private StatusDAO statusDAO;
    private static final double ANNUAL_INTEREST_RATE = 0.05; // 5% annual interest rate

    // Validation regex patterns
    private static final Map<String, Pattern> VALIDATION_PATTERNS = new HashMap<>();
    static {
        VALIDATION_PATTERNS.put("email", Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"));
        VALIDATION_PATTERNS.put("telephone", Pattern.compile("^0[567]\\d{8}$"));
        VALIDATION_PATTERNS.put("nom", Pattern.compile("^[A-Za-zÀ-ÿ\\s'-]{2,50}$"));
        VALIDATION_PATTERNS.put("prenom", Pattern.compile("^[A-Za-zÀ-ÿ\\s'-]{2,50}$"));
        VALIDATION_PATTERNS.put("cin", Pattern.compile("^[A-Z]{1,2}\\d{5,6}$"));
        VALIDATION_PATTERNS.put("revenuMensuel", Pattern.compile("^\\d+(\\.\\d{1,2})?$"));
    }

    @Override
    public void submitCreditRequest(CreditRequest creditRequest) throws ValidationException {
        validateCreditRequest(creditRequest);
        calculateLoan(creditRequest);

        creditRequest.setRequestDate(LocalDateTime.now());
        creditRequest.setRequestNumber(generateUniqueRequestNumber());

        Status pendingStatus = statusDAO.findByName("PENDING");
        if (pendingStatus == null) {
            pendingStatus = new Status("PENDING");
            statusDAO.save(pendingStatus);
        }

        RequestStatus initialStatus = new RequestStatus();
        initialStatus.setCreditRequest(creditRequest);
        initialStatus.setStatus(pendingStatus);
        initialStatus.setModificationDate(LocalDateTime.now());
        initialStatus.setExplanation("Initial submission");

        creditRequest.addRequestStatus(initialStatus);

        creditRequestDAO.save(creditRequest);
    }

    private void validateCreditRequest(CreditRequest creditRequest) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        validateField("email", creditRequest.getEmail(), errors);
        validateField("telephone", creditRequest.getPhoneNumber(), errors);
        validateField("nom", creditRequest.getLastName(), errors);
        validateField("prenom", creditRequest.getFirstName(), errors);
        validateField("cin", creditRequest.getIdentificationNumber(), errors);
        validateField("revenuMensuel", String.valueOf(creditRequest.getMonthlyIncome()), errors);

        validateAmount(creditRequest.getAmount(), errors);
        validateDuration(creditRequest.getDuration(), errors);
        validateAge(creditRequest.getDateOfBirth(), errors);
        validateEmploymentDuration(creditRequest.getEmploymentStartDate(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed", errors);
        }
    }

    private void calculateLoan(CreditRequest creditRequest) {
        int duree = creditRequest.getDuration();
        double tauxMensuel = ANNUAL_INTEREST_RATE / 12;

        double mensualite = creditRequest.getMonthlyPayment();
        double calculatedMontant = (mensualite * (1 - Math.pow(1 + tauxMensuel, -duree))) / tauxMensuel;
        creditRequest.setAmount(Math.round(calculatedMontant * 100.0) / 100.0); // Round to 2 decimal places
    }

    private void validateField(String fieldName, String value, Map<String, String> errors) {
        Pattern pattern = VALIDATION_PATTERNS.get(fieldName);
        if (pattern != null && !pattern.matcher(value).matches()) {
            errors.put(fieldName, "Invalid " + fieldName);
        }
    }

    private void validateAmount(Double amount, Map<String, String> errors) {
        if (amount == null || amount < 1000 || amount > 50000) {
            errors.put("amount", "Amount must be between 1,000 and 50,000");
        }
    }

    private void validateDuration(Integer duration, Map<String, String> errors) {
        if (duration == null || duration < 12 || duration > 60) {
            errors.put("duration", "Duration must be between 12 and 60 months");
        }
    }

    private void validateAge(LocalDate dateOfBirth, Map<String, String> errors) {
        if (dateOfBirth == null) {
            errors.put("dateOfBirth", "Date of birth is required");
        } else {
            int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
            if (age < 18 || age > 65) {
                errors.put("age", "Age must be between 18 and 65");
            }
        }
    }

    private void validateEmploymentDuration(LocalDate employmentStartDate, Map<String, String> errors) {
        if (employmentStartDate == null) {
            errors.put("employmentStartDate", "Employment start date is required");
        } else {
            int employmentDuration = Period.between(employmentStartDate, LocalDate.now()).getYears();
            if (employmentDuration < 1) {
                errors.put("employmentDuration", "Minimum employment duration is 1 year");
            }
        }
    }

    private String generateUniqueRequestNumber() {
        return "REQ-" + System.currentTimeMillis();
    }


    /* Credit List menu */
    @Override
    public List<CreditRequest> getAllCreditRequests() {
        return creditRequestDAO.findAll();
    }

    @Override
    public CreditRequest getCreditRequestDetails(Long id) {
        return creditRequestDAO.findById(id);
    }

    @Override
    public void updateCreditRequestStatus(Long id, String newStatus) {
        CreditRequest request = creditRequestDAO.findById(id);
        if (request == null) {
            throw new IllegalArgumentException("Credit request not found");
        }
        Status status = statusDAO.findByName(newStatus);
        if (status == null) {
            throw new IllegalArgumentException("Invalid status");
        }
        RequestStatus newRequestStatus = new RequestStatus(request, status, LocalDateTime.now(), "Status updated");
        request.addRequestStatus(newRequestStatus);
        creditRequestDAO.update(request);
    }

    @Override
    public List<CreditRequest> filterCreditRequests(LocalDateTime date, String status) {
        return creditRequestDAO.findByDateAndStatus(date, status);
    }
}
