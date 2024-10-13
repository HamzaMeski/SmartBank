package com.smartbank.web.servlets;

import com.smartbank.business.CreditRequestService;
import com.smartbank.business.impl.CreditRequestServiceImpl;
import com.smartbank.dao.impl.CreditRequestDAOImpl;
import com.smartbank.dao.impl.StatusDAOImpl;
import com.smartbank.model.CreditRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/requestDetails")
public class RequestDetailsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RequestDetailsServlet.class.getName());
    private CreditRequestService creditRequestService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        creditRequestService = new CreditRequestServiceImpl(new CreditRequestDAOImpl(), new StatusDAOImpl());
        objectMapper = new ObjectMapper();
    }

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
}
