package com.smartbank.web.servlets;

import com.smartbank.business.CreditService;
import com.smartbank.business.CreditServiceImpl;
import com.smartbank.dao.CreditRequestDAO;
import com.smartbank.dao.impl.CreditRequestDAOImpl;
import com.smartbank.model.CreditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/creditRequests")
public class CreditRequestsServlet extends HttpServlet {
    private CreditService creditService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        CreditRequestDAO creditRequestDAO = new CreditRequestDAOImpl();
        this.creditService = new CreditServiceImpl(creditRequestDAO);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dateStr = request.getParameter("date");
        String status = request.getParameter("status");

        List<CreditRequest> requests;
        if (dateStr != null && !dateStr.isEmpty() && status != null && !status.isEmpty()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                requests = creditService.getCreditRequestsByDateAndStatus(date, status);
            } catch (ParseException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format");
                return;
            }
        } else {
            requests = creditService.getAllCreditRequests();
        }

        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(requests));
    }
}
