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
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/requestDetails")
public class RequestDetailsServlet extends HttpServlet {
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
        CreditRequest creditRequest = creditRequestService.getCreditRequestDetails(id);

        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(creditRequest));
    }
}
