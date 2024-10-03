package com.smartbank.web.servlets;

import com.smartbank.business.CreditService;
import com.smartbank.business.CreditServiceImpl;
import com.smartbank.dao.CreditRequestDAO;
import com.smartbank.dao.impl.CreditRequestDAOImpl;
import com.smartbank.model.CreditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbank.model.CreditStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@WebServlet("/creditSimulation")
public class CreditSimulationServlet extends HttpServlet {
    private CreditService creditService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        CreditRequestDAO creditRequestDAO = new CreditRequestDAOImpl();
        this.creditService = new CreditServiceImpl(creditRequestDAO);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setNumero(UUID.randomUUID().toString());
        creditRequest.setDate(new Date());
        creditRequest.setEtat(CreditStatus.PENDING);
        creditRequest.setMontant(Double.parseDouble(request.getParameter("montant")));
        creditRequest.setDuree(Integer.parseInt(request.getParameter("duree")));
        creditRequest.setRemarques(request.getParameter("remarques"));

        creditService.submitCreditRequest(creditRequest);

        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(creditRequest));
    }
}
