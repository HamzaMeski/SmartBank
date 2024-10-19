package com.smartbank.business;

import com.smartbank.business.impl.CreditRequestServiceImpl;
import com.smartbank.dao.CreditRequestDAO;
import com.smartbank.dao.StatusDAO;
import com.smartbank.model.CreditRequest;
import com.smartbank.model.Status;
import com.smartbank.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreditRequestServiceImplTest {

    @InjectMocks
    private CreditRequestServiceImpl creditRequestService;

    @Mock
    private CreditRequestDAO creditRequestDAO;

    @Mock
    private StatusDAO statusDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(statusDAO.findByName("PENDING")).thenReturn(new Status("PENDING"));
    }

    private CreditRequest createValidCreditRequest(double monthlyPayment, int duration) {
        CreditRequest request = new CreditRequest();
        request.setMonthlyPayment(monthlyPayment);
        request.setDuration(duration);
        request.setEmail("test@example.com");
        request.setPhoneNumber("0612345678");
        request.setLastName("Doe");
        request.setFirstName("John");
        request.setIdentificationNumber("AB123456");
        request.setMonthlyIncome(5000.0);
        request.setDateOfBirth(LocalDate.now().minusYears(30));
        request.setEmploymentStartDate(LocalDate.now().minusYears(2));
        request.setAmount(20000.0);
        return request;
    }

    @Test
    void submitCreditRequest_standardScenario() throws ValidationException {
        CreditRequest request = createValidCreditRequest(1000.0, 12);
        creditRequestService.submitCreditRequest(request);
        assertEquals(11681.22, request.getAmount(), 0.01);
        verify(creditRequestDAO).save(request);
    }

    @Test
    void submitCreditRequest_longerDuration() throws ValidationException {
        CreditRequest request = createValidCreditRequest(1000.0, 60);
        creditRequestService.submitCreditRequest(request);
        assertEquals(52990.71, request.getAmount(), 0.01);
        verify(creditRequestDAO).save(request);
    }

    @Test
    void submitCreditRequest_smallerMonthlyPayment() throws ValidationException {
        CreditRequest request = createValidCreditRequest(500.0, 24);
        creditRequestService.submitCreditRequest(request);
        assertEquals(11396.95, request.getAmount(), 0.01);
        verify(creditRequestDAO).save(request);
    }

    @Test
    void submitCreditRequest_largeMonthlyPayment() throws ValidationException {
        CreditRequest request = createValidCreditRequest(5000.0, 36);
        creditRequestService.submitCreditRequest(request);
        assertEquals(166828.51, request.getAmount(), 0.01);
        verify(creditRequestDAO).save(request);
    }

    @Test
    void submitCreditRequest_shortDuration() throws ValidationException {
        CreditRequest request = createValidCreditRequest(2000.0, 12);
        creditRequestService.submitCreditRequest(request);
        assertEquals(23362.44, request.getAmount(), 0.01);
        verify(creditRequestDAO).save(request);
    }

    @Test
    void submitCreditRequest_verifyRounding() throws ValidationException {
        CreditRequest request = createValidCreditRequest(1234.56, 18);
        creditRequestService.submitCreditRequest(request);
        assertEquals(21366.37, request.getAmount(), 0.01);
        verify(creditRequestDAO).save(request);
    }
}