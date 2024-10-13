package com.smartbank.web.servlets;

import com.smartbank.business.CreditRequestService;
import com.smartbank.business.impl.CreditRequestServiceImpl;
import com.smartbank.dao.impl.CreditRequestDAOImpl;
import com.smartbank.dao.impl.StatusDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/updateStatus")
public class UpdateStatusServlet extends HttpServlet {
    private CreditRequestService creditRequestService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        creditRequestService = new CreditRequestServiceImpl(new CreditRequestDAOImpl(), new StatusDAOImpl());
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String newStatus = request.getParameter("status");

        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            creditRequestService.updateCreditRequestStatus(id, newStatus);
            jsonResponse.put("success", true);
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", e.getMessage());
        }

        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
    }
}
