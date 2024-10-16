package com.smartbank.web.servlets;

import com.smartbank.business.CreditRequestService;
import com.smartbank.business.impl.CreditRequestServiceImpl;
import com.smartbank.dao.impl.CreditRequestDAOImpl;
import com.smartbank.dao.impl.StatusDAOImpl;
import com.smartbank.model.CreditRequest;
import com.smartbank.model.RequestStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jakarta.inject.Inject;

@WebServlet("/requestDetails")
public class RequestDetailsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RequestDetailsServlet.class.getName());
    @Inject
    private CreditRequestService creditRequestService;
    @Inject
    private ObjectMapper objectMapper;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));

        // Get the credit request details
        CreditRequest creditRequest = creditRequestService.getCreditRequestDetails(id);

        // Log the credit request details
        logCreditRequest(creditRequest);

        // Respond with the credit request details as JSON
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(creditRequest));
    }

    // Method to log the credit request details
    private void logCreditRequest(CreditRequest creditRequest) {
        try {
            // Log the credit request as a JSON string for better readability
            String creditRequestJson = objectMapper.writeValueAsString(creditRequest);
            LOGGER.log(Level.INFO, "Credit Request Details: {0}", creditRequestJson);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to log Credit Request", e);
        }
    }

    private static class CreditRequestSerializer extends JsonSerializer<CreditRequest> {
        @Override
        public void serialize(CreditRequest creditRequest, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField("id", creditRequest.getId());
            gen.writeStringField("requestNumber", creditRequest.getRequestNumber());
            gen.writeStringField("project", creditRequest.getProject());
            gen.writeStringField("employmentStatus", creditRequest.getEmploymentStatus());
            gen.writeNumberField("amount", creditRequest.getAmount());
            gen.writeNumberField("duration", creditRequest.getDuration());
            gen.writeNumberField("monthlyPayment", creditRequest.getMonthlyPayment());
            gen.writeStringField("email", creditRequest.getEmail());
            gen.writeStringField("phoneNumber", creditRequest.getPhoneNumber());
            gen.writeStringField("title", creditRequest.getTitle());
            gen.writeStringField("lastName", creditRequest.getLastName());
            gen.writeStringField("firstName", creditRequest.getFirstName());
            gen.writeStringField("identificationNumber", creditRequest.getIdentificationNumber());
            gen.writeStringField("dateOfBirth", creditRequest.getDateOfBirth().toString());
            gen.writeStringField("employmentStartDate", creditRequest.getEmploymentStartDate().toString());
            gen.writeNumberField("monthlyIncome", creditRequest.getMonthlyIncome());
            gen.writeBooleanField("hasOngoingCredits", creditRequest.getHasOngoingCredits());
            gen.writeStringField("requestDate", creditRequest.getRequestDate().toString());

            gen.writeArrayFieldStart("requestStatuses");
            for (RequestStatus status : creditRequest.getRequestStatuses()) {
                gen.writeStartObject();
                gen.writeNumberField("id", status.getId());
                gen.writeStringField("statusName", status.getStatus().getName());
                gen.writeStringField("modificationDate", status.getModificationDate().toString());
                gen.writeStringField("explanation", status.getExplanation());
                gen.writeEndObject();
            }
            gen.writeEndArray();

            gen.writeEndObject();
        }
    }
}
