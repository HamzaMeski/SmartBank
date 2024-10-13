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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/creditRequestList")
public class CreditRequestListServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CreditRequestListServlet.class.getName());
    private CreditRequestService creditRequestService;

    @Override
    public void init() throws ServletException {
        creditRequestService = new CreditRequestServiceImpl(new CreditRequestDAOImpl(), new StatusDAOImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CreditRequest> creditRequests = creditRequestService.getAllCreditRequests();

        // Log the credit requests
        logCreditRequests(creditRequests);

        request.setAttribute("creditRequests", creditRequests);
        request.getRequestDispatcher("/jsp/creditRequestList.jsp").forward(request, response);
    }


    // Helper method to log the credit requests
    private void logCreditRequests(List<CreditRequest> creditRequests) {
        if (creditRequests != null && !creditRequests.isEmpty()) {
            for (CreditRequest request : creditRequests) {
                LOGGER.log(Level.INFO, "Credit Request: ID={0}, Number={1}, Current Status={2}",
                        new Object[]{request.getId(), request.getRequestNumber(), request.getCurrentStatus()});
            }
        } else {
            LOGGER.log(Level.INFO, "No credit requests found.");
        }
    }
}
